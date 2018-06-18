/**
 *  Licensed to ObjectStyle LLC under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ObjectStyle LLC licenses
 *  this file to you under the Apache License, Version 2.0 (the
 *  “License”); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.bootique.linkrest;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.rest.LrFeatureProvider;
import com.nhl.link.rest.LrModuleProvider;
import io.bootique.ModuleExtender;

/**
 * @since 0.15
 */
public class LinkRestModuleExtender extends ModuleExtender<LinkRestModuleExtender> {

    private Multibinder<LrFeatureProvider> featureProviders;
    private Multibinder<LrModuleProvider> moduleProviders;


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
        contributeModuleProviders().addBinding().toInstance(moduleProvider);
        return this;
    }

    /**
     * @param moduleProviderType
     * @return this extender instance.
     * @since 0.25
     */
    public LinkRestModuleExtender addModuleProvider(Class<? extends LrModuleProvider> moduleProviderType) {
        contributeModuleProviders().addBinding().to(moduleProviderType);
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

    private Multibinder<LrFeatureProvider> contributeFeatureProviders() {
        return featureProviders != null ? featureProviders : (featureProviders = newSet(LrFeatureProvider.class));
    }

    private Multibinder<LrModuleProvider> contributeModuleProviders() {
        return moduleProviders != null ? moduleProviders : (moduleProviders = newSet(LrModuleProvider.class));
    }
}
