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

import io.agrest.openapi.AgSwaggerModule;
import io.agrest.runtime.AgBuilder;
import io.bootique.ModuleExtender;
import io.bootique.agrest.v4.AgrestModule;
import io.bootique.agrest.v4.AgrestModuleExtender;
import io.bootique.di.Binder;

/**
 * @since 2.0
 * @deprecated The users are encouraged to switch to Agrest 5
 */
@Deprecated(since = "3.0", forRemoval = true)
public class AgrestSwaggerModuleExtender extends ModuleExtender<AgrestSwaggerModuleExtender> {

    private AgrestModuleExtender agrestExtender;

    public AgrestSwaggerModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public AgrestSwaggerModuleExtender initAllExtensions() {
        return this;
    }

    public AgrestSwaggerModuleExtender addModelPackage(String packageName) {
        agrestExtender().addBuilderCallback(b -> addPackage(b, packageName));
        return this;
    }

    public AgrestSwaggerModuleExtender addModelPackage(Class<?> typeInPackage) {
        Package p = typeInPackage.getPackage();
        if (p != null) {
            agrestExtender().addBuilderCallback(b -> addPackage(b, p.getName()));
        }

        return this;
    }

    public AgrestSwaggerModuleExtender addModelPackage(Package aPackage) {
        agrestExtender().addBuilderCallback(b -> addPackage(b, aPackage.getName()));
        return this;
    }

    private AgBuilder addPackage(AgBuilder builder, String packageName) {
        return builder.module(b -> AgSwaggerModule.contributeEntityPackages(b).add(packageName));
    }

    private AgrestModuleExtender agrestExtender() {
        return agrestExtender != null ? agrestExtender : (agrestExtender = AgrestModule.extend(binder));
    }
}
