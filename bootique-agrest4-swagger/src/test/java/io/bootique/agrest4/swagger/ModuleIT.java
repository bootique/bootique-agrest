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
package io.bootique.agrest4.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.agrest4.swagger.api.TestApi;
import io.bootique.agrest4.swagger.model.P1;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@BQTest
public class ModuleIT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique.app("-s")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .module(b -> BQCoreModule.extend(b).setProperty("bq.swagger.specs.default.pathJson", "spec/oapi.json"))
            .module(b -> BQCoreModule.extend(b).setProperty("bq.swagger.specs.default.resourcePackages[0]", "io.bootique.agrest4.swagger.api"))
            .module(b -> JerseyModule.extend(b).addResource(TestApi.class))
            .module(b -> AgrestSwaggerModule.extend(b).addModelPackage(P1.class))
            .createRuntime();

    static JsonNode model;

    @BeforeAll
    static void readModel() {
        Response r = jetty.getTarget().path("spec/oapi.json").request().get();
        String jsonString = JettyTester.assertOk(r).getContentAsString();
        try {
            model = new ObjectMapper().readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(model);
    }

    @Test
    public void testGet() {

        // TODO: use JSONPath for assertions clarity

        JsonNode get = model.get("paths").get("/agrest").get("get");

        JsonNode params = get.get("parameters");
        assertNotNull(params);

        List<String> expectedParams = asList("cayenneExp", "dir", "exclude", "exp", "include", "limit", "mapBy", "sort", "start");
        assertEquals(expectedParams.size(), params.size());
        params.forEach(p -> assertTrue(expectedParams.contains(p.get("name").asText())));

        JsonNode schemas = model.get("components").get("schemas");
        assertEquals(2, schemas.size());
        assertNotNull(schemas.get("DataResponse(P1)"));

        JsonNode p1 = schemas.get("P1");
        assertNotNull(p1);

        // make sure only Agrest properties are included
        JsonNode p1Props = p1.get("properties");
        assertEquals(1, p1Props.size(), "Only expecting Agrest properties here");
        assertNotNull(p1Props.get("a"));
    }
}
