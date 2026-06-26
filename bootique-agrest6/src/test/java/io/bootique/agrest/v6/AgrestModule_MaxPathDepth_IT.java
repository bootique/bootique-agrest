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

package io.bootique.agrest.v6;

import io.agrest.DataResponse;
import io.agrest.annotation.AgId;
import io.agrest.jaxrs.AgJaxrs;
import io.agrest.meta.AgEntity;
import io.agrest.meta.AgEntityOverlay;
import io.agrest.runtime.processor.select.SelectContext;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit.JettyTester;
import io.bootique.junit.BQApp;
import io.bootique.junit.BQTest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

@BQTest
public class AgrestModule_MaxPathDepth_IT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique
            .app("-s")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .module(b -> AgrestModule.extend(b).maxPathDepth(1))
            .module(b -> JerseyModule.extend(b).addApiResource(R1.class))
            .createRuntime();

    private static E1 e1(int id) {
        E1 e1 = new E1();
        e1.setId(id);
        return e1;
    }

    private static List<E1> fillData(SelectContext<E1> context) {
        return Collections.singletonList(e1(1));
    }

    @Test
    public void withinDepth() {
        Response response = jetty.getTarget().path("r1").queryParam("include", "related.id").request().get();
        JettyTester.assertOk(response).assertContent("""
                {"data":[{"id":1,"related":{"id":2}}],"total":1}""");
    }

    @Test
    public void exceedsDepth() {
        Response response = jetty.getTarget().path("r1").queryParam("include", "related.related.id").request().get();
        JettyTester.assertOk(response).assertContent("""
                {"data":[{"id":1,"related":{}}],"total":1}""");
    }

    @Path("/r1")
    public static class R1 {

        @Context
        private Configuration config;

        @GET
        public DataResponse<E1> get(@Context UriInfo uriInfo) {
            AgEntityOverlay<E1> overlay = AgEntity.overlay(E1.class)
                    .dataResolver(AgrestModule_MaxPathDepth_IT::fillData)
                    .toOne("related", E1.class, e -> e1(e.getId() + 1));

            return AgJaxrs
                    .select(E1.class, config)
                    .clientParams(uriInfo.getQueryParameters())
                    .entityOverlay(overlay)
                    .get();
        }
    }

    public static class E1 {

        private int id;
        private E1 related;

        @AgId
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public E1 getRelated() {
            return related;
        }

        public void setRelated(E1 related) {
            this.related = related;
        }
    }
}
