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

package io.bootique.agrest5;

import io.agrest.DataResponse;
import io.agrest.jaxrs2.AgJaxrs;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.agrest5.cayenne.E1;
import io.bootique.cayenne.v42.CayenneModule;
import io.bootique.cayenne.v42.junit5.CayenneTester;
import io.bootique.jdbc.junit5.derby.DerbyTester;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestTool;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@BQTest
public class AgrestModule_Cayenne_IT {

    @BQTestTool
    static final DerbyTester db = DerbyTester.db();

    @BQTestTool
    static final CayenneTester cayenne = CayenneTester.create().entities(E1.class);

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique
            .app("-s")
            .autoLoadModules()
            .module(db.moduleWithTestDataSource("db"))
            .module(cayenne.moduleWithTestHooks())
            .module(jetty.moduleReplacingConnectors())
            .module(b -> CayenneModule.extend(b).addProject("io/bootique/agrest/cayenne42/cayenne-project.xml"))
            .module(b -> JerseyModule.extend(b).addResource(R1.class))
            .createRuntime();

    @Test
    public void testRequest() {

        db.getTable(cayenne.getTableName(E1.class)).insertColumns("id", "name")
                .values(1, "xyz")
                .exec();

        Response response = jetty.getTarget().path("r1").request().get();
        JettyTester.assertOk(response)
                .assertContent("{\"data\":[{\"id\":1,\"name\":\"xyz\"}],\"total\":1}");
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
    }
}
