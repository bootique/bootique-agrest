package io.bootique.linkrest;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.rest.runtime.adapter.LinkRestAdapter;
import io.bootique.ModuleExtender;

/**
 * @since 0.15
 */
public class LinkRestModuleExtender extends ModuleExtender<LinkRestModuleExtender> {

    private Multibinder<LinkRestAdapter> adapters;

    public LinkRestModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public LinkRestModuleExtender initAllExtensions() {
        contributeAdapters();
        return this;
    }

    public LinkRestModuleExtender addAdapter(LinkRestAdapter adapter) {
        contributeAdapters().addBinding().toInstance(adapter);
        return this;
    }

    public LinkRestModuleExtender addAdapter(Class<? extends LinkRestAdapter> adapterType) {
        contributeAdapters().addBinding().to(adapterType);
        return this;
    }

    protected Multibinder<LinkRestAdapter> contributeAdapters() {
        return adapters != null ? adapters : (adapters = newSet(LinkRestAdapter.class));
    }
}
