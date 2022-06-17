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

package io.bootique.agrest.v5;

import io.agrest.DataResponse;
import io.agrest.annotation.AgAttribute;
import io.agrest.annotation.AgId;
import io.agrest.jaxrs2.AgJaxrs;
import io.agrest.meta.AgEntity;
import io.agrest.runtime.processor.select.SelectContext;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BQTest
public class AgrestModule_Pojo_IT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique
            .app("-s")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .module(b -> JerseyModule.extend(b).addResource(R1.class))
            .createRuntime();

    private static List<E1> fillData(SelectContext<E1> context) {
        E1 e1 = new E1();
        e1.setId(1);
        e1.setName("xyz");
        return Collections.singletonList(e1);
    }

    @Test
    public void testRequest() {
        Response response = jetty.getTarget().path("r1").request().get();
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals("{\"data\":[{\"id\":1,\"name\":\"xyz\"}],\"total\":1}", response.readEntity(String.class));
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
                    .entityOverlay(AgEntity.overlay(E1.class).redefineDataResolver(AgrestModule_Pojo_IT::fillData))
                    .get();
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
