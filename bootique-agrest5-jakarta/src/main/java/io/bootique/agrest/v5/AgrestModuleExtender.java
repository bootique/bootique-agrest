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
import io.bootique.ModuleExtender;
import io.bootique.di.Binder;
import io.bootique.di.SetBuilder;

/**
 * @since 3.0
 */
public class AgrestModuleExtender extends ModuleExtender<AgrestModuleExtender> {

    private SetBuilder<AgModuleProvider> moduleProviders;
    private SetBuilder<AgBuilderCallback> builderCallbacks;

    public AgrestModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public AgrestModuleExtender initAllExtensions() {
        contributeModuleProviders();
        contributeBuilderCallbacks();
        return this;
    }

    /**
     * Sets the policy for the maximum depth of relationship paths in Agrest, such as includes. Depth is counted from
     * the root of the request. Only non-negative depths are allowed. Zero depth blocks all relationships, "1" -
     * blocks anything beyond direct relationships, and so on. Attribute paths are not counted towards depth
     * (either root or nested).
     *
     * @since 3.0
     */
    public AgrestModuleExtender maxPathDepth(int maxPathDepth) {
        return addBuilderCallback(b -> b.maxPathDepth(maxPathDepth));
    }

    /**
     * Configures Agrest runtime to exclude properties with null values from the JSON responses. If this method is not
     * called, nulls will be rendered.
     *
     * @since 3.0
     */
    public AgrestModuleExtender skipNullProperties() {
        return addBuilderCallback(b -> b.skipNullProperties());
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

    private SetBuilder<AgModuleProvider> contributeModuleProviders() {
        return moduleProviders != null ? moduleProviders : (moduleProviders = newSet(AgModuleProvider.class));
    }

    private SetBuilder<AgBuilderCallback> contributeBuilderCallbacks() {
        return builderCallbacks != null ? builderCallbacks : (builderCallbacks = newSet(AgBuilderCallback.class));
    }
}
