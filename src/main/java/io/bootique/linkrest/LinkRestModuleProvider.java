package io.bootique.linkrest;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;
import io.bootique.cayenne.CayenneModuleProvider;
import io.bootique.jersey.JerseyModuleProvider;

import java.util.Collection;

import static java.util.Arrays.asList;

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

    @Override
    public Collection<BQModuleProvider> dependencies() {
        return asList(
                new CayenneModuleProvider(),
                new JerseyModuleProvider()
        );
    }
}
