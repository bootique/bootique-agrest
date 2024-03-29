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
import io.bootique.ModuleExtender;
import io.bootique.di.Binder;
import io.bootique.di.SetBuilder;

/**
 * @deprecated The users are encouraged to switch to Agrest 5
 */
@Deprecated(since = "3.0", forRemoval = true)
public class AgrestModuleExtender extends ModuleExtender<AgrestModuleExtender> {

    private SetBuilder<AgFeatureProvider> featureProviders;
    private SetBuilder<AgModuleProvider> moduleProviders;
    private SetBuilder<AgBuilderCallback> builderCallbacks;

    public AgrestModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public AgrestModuleExtender initAllExtensions() {
        contributeFeatureProviders();
        contributeModuleProviders();
        contributeBuilderCallbacks();
        return this;
    }

    /**
     * @return this extender instance.
     * @since 1.1
     */
    public AgrestModuleExtender addBuilderCallback(AgBuilderCallback callback) {
        contributeBuilderCallbacks().addInstance(callback);
        return this;
    }

    /**
     * @return this extender instance.
     * @since 1.1
     */
    public AgrestModuleExtender addBuilderCallback(Class<? extends AgBuilderCallback> callbackType) {
        contributeBuilderCallbacks().add(callbackType);
        return this;
    }

    /**
     * @return this extender instance.
     */
    public AgrestModuleExtender addModuleProvider(AgModuleProvider moduleProvider) {
        contributeModuleProviders().addInstance(moduleProvider);
        return this;
    }

    /**
     * @return this extender instance.
     */
    public AgrestModuleExtender addModuleProvider(Class<? extends AgModuleProvider> moduleProviderType) {
        contributeModuleProviders().add(moduleProviderType);
        return this;
    }

    /**
     * @return this extender instance.
     */
    public AgrestModuleExtender addFeatureProvider(AgFeatureProvider featureProvider) {
        contributeFeatureProviders().addInstance(featureProvider);
        return this;
    }

    /**
     * @return this extender instance.
     */
    public AgrestModuleExtender addFeatureProvider(Class<? extends AgFeatureProvider> featureProviderType) {
        contributeFeatureProviders().add(featureProviderType);
        return this;
    }

    private SetBuilder<AgFeatureProvider> contributeFeatureProviders() {
        return featureProviders != null ? featureProviders : (featureProviders = newSet(AgFeatureProvider.class));
    }

    private SetBuilder<AgModuleProvider> contributeModuleProviders() {
        return moduleProviders != null ? moduleProviders : (moduleProviders = newSet(AgModuleProvider.class));
    }

    private SetBuilder<AgBuilderCallback> contributeBuilderCallbacks() {
        return builderCallbacks != null ? builderCallbacks : (builderCallbacks = newSet(AgBuilderCallback.class));
    }
}
