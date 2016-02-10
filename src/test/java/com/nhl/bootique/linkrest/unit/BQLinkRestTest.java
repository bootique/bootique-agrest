package com.nhl.bootique.linkrest.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Application;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.map.EntityResolver;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.nhl.bootique.config.ConfigurationFactory;
import com.nhl.bootique.jersey.JerseyModule;
import com.nhl.bootique.linkrest.LinkRestModule;

public abstract class BQLinkRestTest extends JerseyTest {

	protected Injector injector;
	protected ServerRuntime serverRuntimeMock;
	protected EntityResolver entityResolver;

	@Override
	protected Application configure() {

		this.entityResolver = new EntityResolver();

		DataDomain ddMock = mock(DataDomain.class);
		when(ddMock.getEntityResolver()).thenReturn(entityResolver);

		this.serverRuntimeMock = mock(ServerRuntime.class);
		when(serverRuntimeMock.getDataDomain()).thenReturn(ddMock);
		when(serverRuntimeMock.getChannel()).thenReturn(ddMock);

		this.injector = Guice.createInjector(createMockCayenneModule(), createJerseyModule(), createLinkRestModule(),
				createExtrasModule(), createBootiqueModule());
		return injector.getInstance(ResourceConfig.class);
	}

	protected Module createBootiqueModule() {
		return b -> b.bind(ConfigurationFactory.class).toInstance(mock(ConfigurationFactory.class));
	}

	protected Module createMockCayenneModule() {
		return b -> b.bind(ServerRuntime.class).toInstance(serverRuntimeMock);
	}

	protected Module createLinkRestModule() {
		return new LinkRestModule();
	}

	protected JerseyModule createJerseyModule() {
		return new JerseyModule();
	}

	/**
	 * Returns an empty module. Intends to be overridden.
	 * 
	 * @return an empty module.
	 */
	protected Module createExtrasModule() {
		return (b) -> {
		};
	}
}
