package com.nhl.bootique.linkrest;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.rest.meta.LrEntity;
import com.nhl.link.rest.meta.LrEntityBuilder;
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
	public final void adapters(LinkRestAdapter... adapters) {
		Preconditions.checkNotNull(adapters);
		adapters(Arrays.asList(adapters));
	}

	public void adapters(Collection<? extends LinkRestAdapter> adapters) {
		Multibinder<LinkRestAdapter> adapterBinder = Multibinder.newSetBinder(binder, LinkRestAdapter.class);
		adapters.forEach(a -> adapterBinder.addBinding().toInstance(a));
	}

	@SafeVarargs
	public final void adapterTypes(Class<? extends LinkRestAdapter>... adapters) {
		Preconditions.checkNotNull(adapters);
		adapterTypes(Arrays.asList(adapters));
	}

	public void adapterTypes(Collection<Class<? extends LinkRestAdapter>> adapters) {
		Multibinder<LinkRestAdapter> adapterBinder = Multibinder.newSetBinder(binder, LinkRestAdapter.class);
		adapters.forEach(at -> adapterBinder.addBinding().to(at).in(Singleton.class));
	}

	// https://github.com/nhl/link-rest/issues/104 should make this obsolete
	@SafeVarargs
	public final void extraEntityTypes(Class<?>... types) {
		Preconditions.checkNotNull(types);
		extraEntityTypes(Arrays.asList(types));
	}

	// https://github.com/nhl/link-rest/issues/104 should make this obsolete
	public void extraEntityTypes(Collection<Class<?>> types) {
		
		TypeLiteral<LrEntity<?>> tl = new TypeLiteral<LrEntity<?>>() {};
		Multibinder<LrEntity<?>> typeBinder = Multibinder.newSetBinder(binder, tl);
		types.forEach(t -> {
			LrEntity<?> e = LrEntityBuilder.build(t);
			typeBinder.addBinding().toInstance(e);
		});
	}
}
