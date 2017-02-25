package io.bootique.linkrest;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

public class LinkRestModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new LinkRestModule();
	}

	@Override
	public BQModule.Builder moduleBuilder() {
		return BQModuleProvider.super
				.moduleBuilder()
				.description("Provides integration with LinkRest framework.");
	}
}
