package com.nhl.bootique.linkrest;

import java.util.Set;

import org.apache.cayenne.configuration.server.ServerRuntime;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.nhl.bootique.ConfigModule;
import com.nhl.bootique.jersey.JerseyBinder;
import com.nhl.link.rest.meta.LrEntity;
import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;
import com.nhl.link.rest.runtime.adapter.LinkRestAdapter;

public class LinkRestModule extends ConfigModule {

	public LinkRestModule() {
	}

	public LinkRestModule(String configPrefix) {
		super(configPrefix);
	}

	@Override
	public void configure(Binder binder) {
		// 'BQLinkRestFeature' is an injectable wrapper around
		// LinkRestRuntime...
		JerseyBinder.contributeTo(binder).features(BQLinkRestFeature.class);

		// init adapter set without binding any adapters
		LinkRestBinder.contributeTo(binder).adapters();
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
