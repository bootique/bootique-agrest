package io.bootique.linkrest;

import com.nhl.link.rest.DataResponse;
import com.nhl.link.rest.LinkRest;
import com.nhl.link.rest.SelectStage;
import com.nhl.link.rest.annotation.LrAttribute;
import com.nhl.link.rest.annotation.LrId;
import com.nhl.link.rest.runtime.processor.select.SelectContext;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class LinkRestModule_Pojo_IT {

    @Rule
    public JettyTestFactory testFactory = new JettyTestFactory();

    private static void fillData(SelectContext<E1> context) {
        E1 e1 = new E1();
        e1.setId(1);
        e1.setName("xyz");
        context.setObjects(Collections.singletonList(e1));
    }

    @Test
    public void testLRRequest() {
        testFactory.app("-c", "classpath:LinkRestModule_Pojo_IT.yml")
                .autoLoadModules()
                .module(b -> JerseyModule.extend(b).addResource(R1.class))
                .start();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:8080/r1");

        Response response = base.request().get();
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals("{\"data\":[{\"id\":1,\"name\":\"xyz\"}],\"total\":1}", response.readEntity(String.class));
    }

    @Path("/r1")
    public static class R1 {

        @Context
        private Configuration config;

        @GET
        public DataResponse<E1> get(@Context UriInfo uriInfo) {
            return LinkRest
                    .select(E1.class, config)
                    .uri(uriInfo)
                    .terminalStage(SelectStage.APPLY_SERVER_PARAMS, LinkRestModule_Pojo_IT::fillData)
                    .get();
        }
    }

    public static class E1 {

        private int id;
        private String name;

        @LrId
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @LrAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
