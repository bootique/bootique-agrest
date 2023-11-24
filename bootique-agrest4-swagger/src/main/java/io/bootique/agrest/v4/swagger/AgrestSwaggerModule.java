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
package io.bootique.agrest.v4.swagger;

import io.bootique.BQModuleProvider;
import io.bootique.bootstrap.BuiltModule;
import io.bootique.di.BQModule;
import io.bootique.di.Binder;

/**
 * @since 2.0
 * @deprecated The users are encouraged to switch to Agrest 5
 */
@Deprecated(since = "3.0", forRemoval = true)
public class AgrestSwaggerModule implements BQModule, BQModuleProvider {

    public static AgrestSwaggerModuleExtender extend(Binder binder) {
        return new AgrestSwaggerModuleExtender(binder);
    }

    @Override
    public BuiltModule buildModule() {
        return BuiltModule.of(this)
                .provider(this)
                .description("Deprecated and should be replaced with 'bootique-agrest5-jakarta-swagger'.")
                .build();
    }

    @Override
    public void configure(Binder binder) {
    }
}
