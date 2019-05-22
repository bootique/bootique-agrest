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

package io.bootique.agrest.cayenne41;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import io.agrest.AgFeatureProvider;
import io.agrest.AgModuleProvider;
import io.bootique.ModuleExtender;

/**
 * @since 0.15
 */
public class AgrestModuleExtender extends ModuleExtender<AgrestModuleExtender> {

    private Multibinder<AgFeatureProvider> featureProviders;
    private Multibinder<AgModuleProvider> moduleProviders;


    public AgrestModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public AgrestModuleExtender initAllExtensions() {
        contributeFeatureProviders();
        contributeModuleProviders();
        return this;
    }

    /**
     * @param moduleProvider
     * @return this extender instance.
     * @since 0.25
     */
    public AgrestModuleExtender addModuleProvider(AgModuleProvider moduleProvider) {
        contributeModuleProviders().addBinding().toInstance(moduleProvider);
        return this;
    }

    /**
     * @param moduleProviderType
     * @return this extender instance.
     * @since 0.25
     */
    public AgrestModuleExtender addModuleProvider(Class<? extends AgModuleProvider> moduleProviderType) {
        contributeModuleProviders().addBinding().to(moduleProviderType);
        return this;
    }

    /**
     * @param featureProvider
     * @return this extender instance.
     * @since 0.25
     */
    public AgrestModuleExtender addFeatureProvider(AgFeatureProvider featureProvider) {
        contributeFeatureProviders().addBinding().toInstance(featureProvider);
        return this;
    }

    /**
     * @param featureProviderType
     * @return this extender instance.
     * @since 0.25
     */
    public AgrestModuleExtender addFeatureProvider(Class<? extends AgFeatureProvider> featureProviderType) {
        contributeFeatureProviders().addBinding().to(featureProviderType);
        return this;
    }

    private Multibinder<AgFeatureProvider> contributeFeatureProviders() {
        return featureProviders != null ? featureProviders : (featureProviders = newSet(AgFeatureProvider.class));
    }

    private Multibinder<AgModuleProvider> contributeModuleProviders() {
        return moduleProviders != null ? moduleProviders : (moduleProviders = newSet(AgModuleProvider.class));
    }
}
