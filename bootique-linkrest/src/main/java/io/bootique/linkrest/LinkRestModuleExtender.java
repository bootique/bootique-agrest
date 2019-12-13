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

package io.bootique.linkrest;

import com.nhl.link.rest.LrFeatureProvider;
import com.nhl.link.rest.LrModuleProvider;
import io.bootique.ModuleExtender;
import io.bootique.di.Binder;
import io.bootique.di.SetBuilder;

/**
 * @since 0.15
 */
public class LinkRestModuleExtender extends ModuleExtender<LinkRestModuleExtender> {

    private SetBuilder<LrFeatureProvider> featureProviders;
    private SetBuilder<LrModuleProvider> moduleProviders;


    public LinkRestModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public LinkRestModuleExtender initAllExtensions() {
        contributeFeatureProviders();
        contributeModuleProviders();
        return this;
    }

    /**
     * @param moduleProvider
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addModuleProvider(LrModuleProvider moduleProvider) {
        contributeModuleProviders().add(moduleProvider);
        return this;
    }

    /**
     * @param moduleProviderType
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addModuleProvider(Class<? extends LrModuleProvider> moduleProviderType) {
        contributeModuleProviders().add(moduleProviderType);
        return this;
    }

    /**
     * @param featureProvider
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addFeatureProvider(LrFeatureProvider featureProvider) {
        contributeFeatureProviders().add(featureProvider);
        return this;
    }

    /**
     * @param featureProviderType
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addFeatureProvider(Class<? extends LrFeatureProvider> featureProviderType) {
        contributeFeatureProviders().add(featureProviderType);
        return this;
    }

    private SetBuilder<LrFeatureProvider> contributeFeatureProviders() {
        return featureProviders != null ? featureProviders : (featureProviders = newSet(LrFeatureProvider.class));
    }

    private SetBuilder<LrModuleProvider> contributeModuleProviders() {
        return moduleProviders != null ? moduleProviders : (moduleProviders = newSet(LrModuleProvider.class));
    }
}
