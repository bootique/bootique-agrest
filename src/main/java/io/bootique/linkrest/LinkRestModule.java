package io.bootique.linkrest;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.nhl.link.rest.LrFeatureProvider;
import com.nhl.link.rest.LrModuleProvider;
import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;
import io.bootique.ConfigModule;
import io.bootique.jersey.JerseyModule;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.Set;

public class LinkRestModule extends ConfigModule {

    public LinkRestModule() {
    }

    public LinkRestModule(String configPrefix) {
        super(configPrefix);
    }

    /**
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link LinkRestModuleExtender} that can be used to load LinkMove custom extensions.
     * @since 0.15
     */
    public static LinkRestModuleExtender extend(Binder binder) {
        return new LinkRestModuleExtender(binder);
    }

    @Override
    public void configure(Binder binder) {
        // 'BQLinkRestFeature' is an injectable wrapper around LinkRestRuntime...
        JerseyModule.extend(binder).addFeature(BQLinkRestFeature.class);

        // trigger extension points creation and provide default contributions
        LinkRestModule.extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    LinkRestRuntime provideLinkRestRuntime(
            Injector injector,
            Set<LrFeatureProvider> featureProviders,
            Set<LrModuleProvider> moduleProviders) {

        LinkRestBuilder builder;

        Binding<ServerRuntime> binding = injector.getExistingBinding(Key.get(ServerRuntime.class));
        if (binding == null) {
            builder = new LinkRestBuilder().cayenneService(new PojoCayennePersister());
        } else {
            ServerRuntime cayenneRuntime = binding.getProvider().get();
            builder = new LinkRestBuilder().cayenneRuntime(cayenneRuntime);
        }

        featureProviders.forEach(builder::feature);
        moduleProviders.forEach(builder::module);

        return builder.build();
    }
}
