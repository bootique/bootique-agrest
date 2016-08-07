package io.bootique.linkrest;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;
import com.nhl.link.rest.runtime.adapter.LinkRestAdapter;
import io.bootique.ConfigModule;
import io.bootique.jersey.JerseyModule;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.Set;

public class LinkRestModule extends ConfigModule {

	/**
	 * @since 0.6
	 * @param binder
	 *            DI binder passed to the Module that invokes this method.
	 * @return {@link Multibinder} for contributed LinkRest adapters.
	 */
	public static Multibinder<LinkRestAdapter> contributeAdapters(Binder binder) {
		return Multibinder.newSetBinder(binder, LinkRestAdapter.class);
	}

	public LinkRestModule() {
	}

	public LinkRestModule(String configPrefix) {
		super(configPrefix);
	}

	@Override
	public void configure(Binder binder) {
		// 'BQLinkRestFeature' is an injectable wrapper around
		// LinkRestRuntime...
		JerseyModule.contributeFeatures(binder).addBinding().to(BQLinkRestFeature.class);

		// trigger extension points creation and provide default contributions
		LinkRestModule.contributeAdapters(binder);
	}

	@Singleton
	@Provides
	LinkRestRuntime provideLinkRestRuntime(ServerRuntime serverRuntime, Set<LinkRestAdapter> adapters) {
		LinkRestBuilder builder = LinkRestBuilder.builder(serverRuntime);

		adapters.forEach(a -> builder.adapter(a));

		return builder.build();
	}
}
