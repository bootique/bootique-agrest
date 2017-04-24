package io.bootique.linkrest;

import com.google.inject.Module;
import com.nhl.link.rest.DataResponse;
import com.nhl.link.rest.LinkRest;
import com.nhl.link.rest.annotation.LrAttribute;
import com.nhl.link.rest.annotation.LrId;
import com.nhl.link.rest.annotation.listener.SelectServerParamsApplied;
import com.nhl.link.rest.processor.ProcessingStage;
import com.nhl.link.rest.runtime.processor.select.SelectContext;
import io.bootique.jersey.JerseyModule;
import io.bootique.linkrest.unit.BQLinkRestTest;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class LinkRestModule_Pojo_IT extends BQLinkRestTest {

	@Override
	protected Module createExtrasModule() {
		return b -> {
			JerseyModule.extend(b).addResource(R1.class);
		};
	}

	@Test
	public void testLRRequest() {
		Response response = target("/r1").request().get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals("{\"data\":[{\"id\":1,\"name\":\"xyz\"}],\"total\":1}", response.readEntity(String.class));
	}

	@Path("/r1")
	public static class R1 {

		@Context
		private Configuration config;

		@GET
		public DataResponse<E1> get(@Context UriInfo uriInfo) {
			return LinkRest.select(E1.class, config).uri(uriInfo).listener(new PojoListener()).get();
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

	public static class PojoListener {

		@SelectServerParamsApplied
		public ProcessingStage<SelectContext<E1>, E1> altBackend(SelectContext<E1> context) {

			E1 e1 = new E1();
			e1.setId(1);
			e1.setName("xyz");
			context.setObjects(Collections.singletonList(e1));

			// stop further processing
			return null;
		}
	}

}
