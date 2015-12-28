package com.nhl.bootique.linkrest;

import org.apache.cayenne.configuration.server.ServerRuntime;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.nhl.bootique.ConfigModule;
import com.nhl.bootique.jersey.JerseyBinder;
import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;

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
	}

	@Provides
	public LinkRestRuntime createLinkRestRuntime(ServerRuntime serverRuntime) {
		return LinkRestBuilder.builder(serverRuntime).build();
	}
}
