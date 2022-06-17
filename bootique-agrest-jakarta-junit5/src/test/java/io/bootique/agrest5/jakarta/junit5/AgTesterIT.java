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
package io.bootique.agrest5.jakarta.junit5;

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
import io.bootique.agrest.v5.jakarta.AgrestModule;
import io.bootique.jersey.jakarta.JerseyModule;
import io.bootique.jetty.jakarta.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@BQTest
public class AgTesterIT {

    static final JettyTester jetty = JettyTester.create();

    static AgEntityOverlay<E1> e1Overlay = AgEntity.overlay(E1.class)
            .redefineDataResolver(new RootDataResolver<>() {
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
            .module(b -> JerseyModule.extend(b).addResource(R1.class))
            .module(b -> AgrestModule.extend(b).addBuilderCallback(ab -> ab.entityOverlay(e1Overlay)))
            .createRuntime();

    private static void fillData(SelectContext<E1> context) {
        E1 e1 = new E1();
        e1.setId(1);
        e1.setName("xyz");
        context.getEntity().setData(Collections.singletonList(e1));
    }

    @Test
    public void testGet() {
        WebTarget target = jetty.getTarget().path("r1");
        io.bootique.agrest5.jakarta.junit5.AgTester.request(target).get()
                .assertOk()
                .assertContent(1, "{\"id\":1,\"name\":\"xyz\"}");
    }

    @Test
    public void testDelete() {
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
