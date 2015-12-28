package com.nhl.bootique.linkrest;

import com.google.inject.Module;
import com.nhl.bootique.BQModuleProvider;

public class LinkRestModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new LinkRestModule();
	}
}
