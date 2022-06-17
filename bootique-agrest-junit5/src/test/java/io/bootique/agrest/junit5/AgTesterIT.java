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

import io.agrest.Ag;
import io.agrest.DataResponse;
import io.agrest.SimpleResponse;
import io.agrest.annotation.AgAttribute;
import io.agrest.annotation.AgId;
import io.agrest.meta.AgEntity;
import io.agrest.meta.AgEntityOverlay;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@BQTest
public class AgTesterIT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique
            .app("-s")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .module(b -> JerseyModule.extend(b).addResource(R1.class))
            .createRuntime();


    @Test
    public void testGet() {
        WebTarget target = jetty.getTarget().path("r1");
        AgTester.request(target).get()
                .assertOk()
                .assertContent(1, "{\"id\":1,\"name\":\"xyz\"}");
    }

    @Test
    // TODO: 4.x supports Delete stages, so we can implement a POJO backend
    @Disabled("TODO: 4.x supports Delete stages, so we can implement a POJO backend")
    public void testDelete() {
        WebTarget target = jetty.getTarget().path("r1");
        AgTester.request(target).delete()
                .assertOk()
                .assertContent(1, "{\"success\":\"true\"}");
    }

    @Path("/r1")
    public static class R1 {

        @Context
        private Configuration config;

        @GET
        public DataResponse<E1> get(@Context UriInfo uriInfo) {

            AgEntityOverlay<E1> overlay = AgEntity.overlay(E1.class)
                    .redefineDataResolver(c -> {
                        E1 e1 = new E1();
                        e1.setId(1);
                        e1.setName("xyz");
                        return List.of(e1);
                    });

            return Ag
                    .select(E1.class, config)
                    .uri(uriInfo)
                    .entityOverlay(overlay)
                    .get();
        }

        @DELETE
        public SimpleResponse delete(@Context UriInfo uriInfo) {
            return Ag.delete(E1.class, config).sync();
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
