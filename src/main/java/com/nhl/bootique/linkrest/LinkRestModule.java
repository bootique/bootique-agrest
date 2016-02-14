package com.nhl.bootique.linkrest;

import java.util.Set;

import org.apache.cayenne.configuration.server.ServerRuntime;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.nhl.bootique.ConfigModule;
import com.nhl.bootique.jersey.JerseyModule;
import com.nhl.link.rest.meta.LrEntity;
import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;
import com.nhl.link.rest.runtime.adapter.LinkRestAdapter;

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

	/**
	 * Allows to bind extra {@link LrEntity} objects. Most often you'd produce
	 * an LrEntity from an annotated Java class using the following code:
	 * 
	 * <pre>
	 * LrEntity&lt;?&gt; e = LrEntityBuilder.build(MyClass.class);
	 * </pre>
	 * 
	 * @since 0.6
	 * @param binder
	 *            DI binder passed to the Module that invokes this method.
	 * @return {@link Multibinder} for contributing LrEntity's.
	 */
	public static Multibinder<LrEntity<?>> contributeExtraEntities(Binder binder) {
		TypeLiteral<LrEntity<?>> tl = new TypeLiteral<LrEntity<?>>() {
		};
		return Multibinder.newSetBinder(binder, tl);
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

	@Provides
	public LinkRestRuntime createLinkRestRuntime(ServerRuntime serverRuntime, Set<LinkRestAdapter> adapters,
			Set<LrEntity<?>> extraEntities) {
		LinkRestBuilder builder = LinkRestBuilder.builder(serverRuntime);

		adapters.forEach(a -> builder.adapter(a));
		extraEntities.forEach(e -> builder.extraEntity(e));

		return builder.build();
	}
}
