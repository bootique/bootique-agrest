/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.bootique.agrest.junit5;

import io.agrest.DataResponse;
import io.agrest.DeleteStage;
import io.agrest.SimpleResponse;
import io.agrest.annotation.AgAttribute;
import io.agrest.annotation.AgId;
import io.agrest.jaxrs3.AgJaxrs;
import io.agrest.meta.AgEntity;
import io.agrest.meta.AgEntityOverlay;
import io.agrest.resolver.RootDataResolver;
import io.agrest.runtime.processor.select.SelectContext;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.agrest.v5.AgrestModule;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

@BQTest
public class AgTesterIT {

    static final JettyTester jetty = JettyTester.create();

    static AgEntityOverlay<E1> e1Overlay = AgEntity.overlay(E1.class)
            .dataResolver(new RootDataResolver<>() {
                @Override
                public void assembleQuery(SelectContext<E1> context) {
                }

                @Override
                public void fetchData(SelectContext<E1> context) {
                    fillData(context);
                }
            });

    @BQApp
    static final BQRuntime app = Bootique
            .app("-s")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .module(b -> JerseyModule.extend(b).addApiResource(R1.class))
            .module(b -> AgrestModule.extend(b).addBuilderCallback(ab -> ab.entityOverlay(e1Overlay)))
            .createRuntime();

    private static void fillData(SelectContext<E1> context) {

        E1 one = new E1();
        one.setId(1);
        one.setName("xyz");

        E1 two = new E1();
        two.setId(2);
        two.setName("abc");

        context.getEntity().setData(List.of(one, two));
    }

    @Test
    public void get_AssertContent() {
        WebTarget target = jetty.getTarget().path("r1");
        AgTester.request(target).get()
                .assertOk()
                .assertContent(2, "{\"id\":1,\"name\":\"xyz\"}", "{\"id\":2,\"name\":\"abc\"}");
    }

    @Test
    public void assertTotal() {
        WebTarget target = jetty.getTarget().path("r1");
        AgTester.request(target).get()
                .assertOk()
                .assertTotal(2);
    }

    @Test
    public void assertDataSize() {
        WebTarget target = jetty.getTarget().path("r1").queryParam("limit", 1);
        AgTester.request(target).get()
                .assertOk()
                .assertTotal(2)
                .assertDataSize(1);
    }

    @Test
    public void assertContentAt() {
        WebTarget target = jetty.getTarget().path("r1");
        AgTester.request(target).get()
                .assertOk()
                .assertContentAt(0, "{\"id\":1,\"name\":\"xyz\"}")
                .assertContentAt(1, "{\"id\":2,\"name\":\"abc\"}");
    }


    @Test
    public void requestCustomizer() {
        WebTarget target = jetty.getTarget().path("r1/headers");
        AgTester.request(target)
                .customizeRequest(b -> b.header("h1", "v1"))
                .customizeRequest(b -> b.header("h2", "v2"))
                .get()
                .assertOk()
                .assertContent(1, "{\"h1\":\"v1\",\"h2\":\"v2\"}");
    }

    @Test
    public void delete() {
        WebTarget target = jetty.getTarget().path("r1");
        AgTester.request(target).delete()
                .assertOk()
                .assertContent("{}");
    }

    @Path("/r1")
    public static class R1 {

        @Context
        private Configuration config;

        @GET
        @Path("headers")
        public DataResponse<H> getHeaders(@HeaderParam("h1") String h1, @HeaderParam("h2") String h2) {

            AgEntityOverlay<H> overlay = AgEntity
                    .overlay(H.class)
                    .dataResolver(c -> List.of(new H(h1, h2)));

            return AgJaxrs
                    .select(H.class, config)
                    .entityOverlay(overlay)
                    .get();
        }

        @GET
        public DataResponse<E1> get(@Context UriInfo uriInfo) {

            return AgJaxrs
                    .select(E1.class, config)
                    .clientParams(uriInfo.getQueryParameters())
                    .get();
        }

        @DELETE
        public SimpleResponse delete(@Context UriInfo uriInfo) {
            return AgJaxrs
                    .delete(E1.class, config)
                    .terminalStage(DeleteStage.START, c -> {
                    })
                    .sync();
        }
    }

    public static class H {

        private final String h1;
        private final String h2;

        public H(String h1, String h2) {
            this.h1 = h1;
            this.h2 = h2;
        }

        @AgAttribute
        public String getH1() {
            return h1;
        }

        @AgAttribute
        public String getH2() {
            return h2;
        }
    }

    public static class E1 {

        private int id;
        private String name;

        @AgId
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @AgAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
