package io.bootique.linkrest;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

public class LinkRestModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new LinkRestModule();
	}
}
