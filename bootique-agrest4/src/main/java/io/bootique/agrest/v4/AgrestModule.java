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

package io.bootique.agrest.v4;

import io.agrest.AgFeatureProvider;
import io.agrest.AgModuleProvider;
import io.agrest.cayenne.AgCayenneBuilder;
import io.agrest.runtime.AgBuilder;
import io.agrest.runtime.AgRuntime;
import io.bootique.ConfigModule;
import io.bootique.ModuleCrate;
import io.bootique.di.Binder;
import io.bootique.di.Injector;
import io.bootique.di.Provides;
import io.bootique.jersey.JerseyModule;
import org.apache.cayenne.configuration.server.ServerRuntime;

import javax.inject.Singleton;
import java.util.Set;

/**
 * @deprecated The users are encouraged to switch to Agrest 5
 */
@Deprecated(since = "3.0", forRemoval = true)
public class AgrestModule extends ConfigModule {

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
                .description("Deprecated, can be replaced with 'bootique-agrest5-jakarta'.")
                .build();
    }

    @Override
    public void configure(Binder binder) {
        // 'BQAgrestFeature' is an injectable wrapper around AgRuntime...
        JerseyModule.extend(binder).addFeature(BQAgrestFeature.class);

        // trigger extension points creation and provide default contributions
        AgrestModule.extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    AgRuntime provideAgRuntime(
            Injector injector,
            Set<AgBuilderCallback> builderCallbacks,
            Set<AgFeatureProvider> featureProviders,
            Set<AgModuleProvider> moduleProviders) {

        AgBuilder builder = createBuilder(injector);
        featureProviders.forEach(builder::feature);
        moduleProviders.forEach(builder::module);
        builderCallbacks.forEach(c -> c.configure(builder));

        return builder.build();
    }

    private AgBuilder createBuilder(Injector injector) {
        AgBuilder builder = new AgBuilder();

        if (injector.hasProvider(ServerRuntime.class)) {
            ServerRuntime runtime = injector.getInstance(ServerRuntime.class);
            builder.module(AgCayenneBuilder.builder().runtime(runtime).build());
        }

        return builder;
    }
}
