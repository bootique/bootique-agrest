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
package io.bootique.agrest.v5.swagger;

import io.agrest.jaxrs2.openapi.modelconverter.AgEntityModelConverter;
import io.agrest.jaxrs2.openapi.modelconverter.AgProtocolModelConverter;
import io.agrest.jaxrs2.openapi.modelconverter.AgValueModelConverter;
import io.agrest.runtime.AgRuntime;
import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.swagger.SwaggerModule;
import io.swagger.v3.core.util.PrimitiveType;

import jakarta.inject.Singleton;

/**
 * @since 3.0
 * @deprecated The users are encouraged to switch to Jakarta-based flavor
 */
@Deprecated(since = "3.0", forRemoval = true)
public class AgrestSwaggerModule implements BQModule {

    public static AgrestSwaggerModuleExtender extend(Binder binder) {
        return new AgrestSwaggerModuleExtender(binder);
    }

    @Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description("Deprecated, can be replaced with 'bootique-agrest5-jakarta-swagger'.")
                .build();
    }

    @Override
    public void configure(Binder binder) {

        // The following loosely copied from Agrest AgSwaggerModuleInstaller. Agrest runs it as a JAX-RS feature, but
        // we need to enable converters and custom features regardless of whether the generator was started in the
        // JAX-RS environment or via "--generate-spec" command

        PrimitiveType.enablePartialTime();

        SwaggerModule.extend(binder)
                .addModelConverter(AgValueModelConverter.class)
                .addModelConverter(AgProtocolModelConverter.class)
                .addModelConverter(AgEntityModelConverter.class);
    }

    @Provides
    @Singleton
    AgValueModelConverter provideValueModelConverter(AgRuntime agrest) {
        return agrest.service(AgValueModelConverter.class);
    }

    @Provides
    @Singleton
    AgProtocolModelConverter provideProtocolModelConverter(AgRuntime agrest) {
        return agrest.service(AgProtocolModelConverter.class);
    }

    @Provides
    @Singleton
    AgEntityModelConverter provideEntityModelConverter(AgRuntime agrest) {
        return agrest.service(AgEntityModelConverter.class);
    }
}
