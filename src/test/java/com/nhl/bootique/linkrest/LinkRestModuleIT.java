package com.nhl.bootique.linkrest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;

import com.google.inject.Module;
import com.nhl.bootique.jersey.JerseyModule;
import com.nhl.bootique.linkrest.unit.BQLinkRestTest;
import com.nhl.link.rest.DataResponse;
import com.nhl.link.rest.LinkRest;
import com.nhl.link.rest.annotation.LrAttribute;
import com.nhl.link.rest.annotation.LrId;

public class LinkRestModuleIT extends BQLinkRestTest {

	@Override
	protected JerseyModule createJerseyModule() {
		return super.createJerseyModule().resource(R1.class);
	}

	@Override
	protected Module createExtrasModule() {
		return b -> LinkRestBinder.contributeTo(b).extraEntityTypes(E1.class);
	}

	@Test
	public void testLRRequest() {
		Response response = target("/r1").request().get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals("{\"data\":[{\"id\":1}],\"total\":1}", response.readEntity(String.class));
	}

	@Path("/r1")
	public static class R1 {

		@Context
		private Configuration config;

		@GET
		public DataResponse<E1> get(@Context UriInfo uriInfo) {
			return LinkRest.select(E1.class, config).uri(uriInfo).select();
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
