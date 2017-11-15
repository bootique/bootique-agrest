package io.bootique.linkrest;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.rest.LrFeatureProvider;
import com.nhl.link.rest.runtime.adapter.LinkRestAdapter;
import io.bootique.ModuleExtender;

/**
 * @since 0.15
 */
public class LinkRestModuleExtender extends ModuleExtender<LinkRestModuleExtender> {

    @Deprecated
    private Multibinder<LinkRestAdapter> adapters;
    private Multibinder<LrFeatureProvider> featureProviders;


    public LinkRestModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public LinkRestModuleExtender initAllExtensions() {
        contributeAdapters();
        contributeFeatureProviders();
        return this;
    }

    /**
     * @param featureProvider
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addFeatureProvider(LrFeatureProvider featureProvider) {
        contributeFeatureProviders().addBinding().toInstance(featureProvider);
        return this;
    }

    /**
     * @param featureProviderType
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addFeatureProvider(Class<? extends LrFeatureProvider> featureProviderType) {
        contributeFeatureProviders().addBinding().to(featureProviderType);
        return this;
    }

    /**
     * @param adapter
     * @return this extender instance.
     * @deprecated since 0.25 as {@link LinkRestAdapter} is deprecated. Use other extension methods to customize
     * LinkRest stack.
     */
    @Deprecated
    public LinkRestModuleExtender addAdapter(LinkRestAdapter adapter) {
        contributeAdapters().addBinding().toInstance(adapter);
        return this;
    }

    /**
     * @param adapterType
     * @return this extender instance.
     * @deprecated since 0.25 as {@link LinkRestAdapter} is deprecated. Use other extension methods to customize
     * LinkRest stack.
     */
    @Deprecated
    public LinkRestModuleExtender addAdapter(Class<? extends LinkRestAdapter> adapterType) {
        contributeAdapters().addBinding().to(adapterType);
        return this;
    }

    @Deprecated
    protected Multibinder<LinkRestAdapter> contributeAdapters() {
        return adapters != null ? adapters : (adapters = newSet(LinkRestAdapter.class));
    }

    private Multibinder<LrFeatureProvider> contributeFeatureProviders() {
        return featureProviders != null ? featureProviders : (featureProviders = newSet(LrFeatureProvider.class));
    }
}
