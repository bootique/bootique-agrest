/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.agrest.v5;

import io.agrest.AgModuleProvider;
import io.agrest.cayenne.AgCayenneModule;
import io.agrest.jaxrs3.AgJaxrsFeature;
import io.agrest.meta.AgEntityOverlay;
import io.agrest.runtime.AgRuntime;
import io.agrest.runtime.AgRuntimeBuilder;
import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.di.Binder;
import io.bootique.di.Injector;
import io.bootique.di.Provides;
import io.bootique.jersey.JerseyModule;
import org.apache.cayenne.configuration.server.ServerRuntime;

import jakarta.inject.Singleton;
import java.util.Set;

/**
 * @since 3.0
 */
public class AgrestModule implements BQModule {

    /**
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link AgrestModuleExtender} that can be used to load LinkMove custom extensions.
     */
    public static AgrestModuleExtender extend(Binder binder) {
        return new AgrestModuleExtender(binder);
    }

    @Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description("Integrates with Agrest framework v5")
                .build();
    }

    @Override
    public void configure(Binder binder) {
        JerseyModule.extend(binder).addFeature(AgJaxrsFeature.class);

        // trigger extension points creation and provide default contributions
        AgrestModule.extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    AgRuntime provideAgRuntime(
            Injector injector,
            Set<AgModuleProvider> moduleProviders,
            Set<AgEntityOverlay> entityOverlays,
            Set<AgBuilderCallback> builderCallbacks) {

        AgRuntimeBuilder builder = createBuilder(injector);
        moduleProviders.forEach(builder::module);

        // add explicit overlays, and then apply custom callbacks
        entityOverlays.forEach(builder::entityOverlay);

        builderCallbacks.forEach(c -> c.configure(builder));

        return builder.build();
    }

    @Singleton
    @Provides
    AgJaxrsFeature provideAgJaxrsFeature(AgRuntime runtime) {
        return AgJaxrsFeature.builder(runtime).build();
    }

    private AgRuntimeBuilder createBuilder(Injector injector) {
        AgRuntimeBuilder builder = AgRuntime.builder();

        if (injector.hasProvider(ServerRuntime.class)) {
            ServerRuntime cayenneRuntime = injector.getInstance(ServerRuntime.class);
            builder.module(AgCayenneModule.build(cayenneRuntime));
        }

        return builder;
    }
}
