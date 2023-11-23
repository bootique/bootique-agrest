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
package io.bootique.agrest.v5.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.agrest.v5.swagger.api.TestApi;
import io.bootique.agrest.v5.swagger.model.P1;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@BQTest
public class ModuleIT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique.app("-s")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .module(b -> BQCoreModule.extend(b).setProperty("bq.swagger.specs.default.pathJson", "spec/oapi.json"))
            .module(b -> BQCoreModule.extend(b).setProperty("bq.swagger.specs.default.resourcePackages[0]", "io.bootique.agrest.v5.swagger.api"))
            .module(b -> JerseyModule.extend(b).addResource(TestApi.class))
            .module(b -> AgrestSwaggerModule.extend(b).entityPackage(P1.class))
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
    public void get() {

        // TODO: use JSONPath for assertions clarity

        JsonNode get = model.get("paths").get("/agrest").get("get");

        JsonNode params = get.get("parameters");
        assertNotNull(params);

        Set<String> expectedParams = Set.of("direction", "exclude", "exp", "include", "limit", "mapBy", "sort", "start");
        Set<String> actualParams = stream(params).map(n -> n.get("name").asText()).collect(Collectors.toSet());
        assertEquals(expectedParams, actualParams);

        JsonNode schemas = model.get("components").get("schemas");
        assertEquals(2, schemas.size());
        assertNotNull(schemas.get("DataResponse(P1)"));

        JsonNode p1 = schemas.get("P1");
        assertNotNull(p1);

        Set<String> expectedP1Props = Set.of("a");
        Set<String> actualP1Props = stream(p1.get("properties").fieldNames()).collect(Collectors.toSet());
        assertEquals(expectedP1Props, actualP1Props, "Only Agrest properties must be included in the Model");
    }

    private <T> Stream<T> stream(Iterable<T> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }

    private <T> Stream<T> stream(Iterator<T> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }
}
