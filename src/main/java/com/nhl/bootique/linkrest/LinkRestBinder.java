package com.nhl.bootique.linkrest;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.rest.runtime.adapter.LinkRestAdapter;

public class LinkRestBinder {

	public static LinkRestBinder contributeTo(Binder binder) {
		return new LinkRestBinder(binder);
	}

	private Binder binder;

	LinkRestBinder(Binder binder) {
		this.binder = binder;
	}

	@SafeVarargs
	public final void adapters(Class<? extends LinkRestAdapter>... adapters) {
		Preconditions.checkNotNull(adapters);
		adapters(Arrays.asList(adapters));
	}

	public void adapters(Collection<Class<? extends LinkRestAdapter>> adapters) {
		Multibinder<LinkRestAdapter> adapterBinder = Multibinder.newSetBinder(binder, LinkRestAdapter.class);
		adapters.forEach(jt -> adapterBinder.addBinding().to(jt).in(Singleton.class));
	}
}
