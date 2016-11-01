package io.bootique.linkrest.unit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.bootique.config.ConfigurationFactory;
import io.bootique.jersey.JerseyModule;
import io.bootique.linkrest.LinkRestModule;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.core.Application;

import static org.mockito.Mockito.mock;

public abstract class BQLinkRestTest extends JerseyTest {

	protected Injector injector;

	@Override
	protected Application configure() {

		this.injector = Guice.createInjector(new JerseyModule(), createLinkRestModule(),
				createExtrasModule(), createBootiqueModule());
		return injector.getInstance(ResourceConfig.class);
	}

	protected Module createBootiqueModule() {
		return b -> b.bind(ConfigurationFactory.class).toInstance(mock(ConfigurationFactory.class));
	}

	protected Module createLinkRestModule() {
		return new LinkRestModule();
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
