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
package io.bootique.agrest5.jakarta.swagger;

import io.agrest.jaxrs3.openapi.AgSwaggerModule;
import io.agrest.runtime.AgRuntimeBuilder;
import io.bootique.ModuleExtender;
import io.bootique.agrest5.jakarta.AgrestModule;
import io.bootique.agrest5.jakarta.AgrestModuleExtender;
import io.bootique.di.Binder;

/**
 * @since 3.0
 */
public class AgrestSwaggerModuleExtender extends ModuleExtender<AgrestSwaggerModuleExtender> {

    private AgrestModuleExtender agrestExtender;

    public AgrestSwaggerModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public AgrestSwaggerModuleExtender initAllExtensions() {
        return this;
    }

    public AgrestSwaggerModuleExtender entityPackage(String packageName) {
        agrestExtender().addBuilderCallback(b -> entityPackage(b, packageName));
        return this;
    }

    public AgrestSwaggerModuleExtender entityPackage(Class<?> typeInPackage) {
        Package p = typeInPackage.getPackage();
        if (p != null) {
            agrestExtender().addBuilderCallback(b -> entityPackage(b, p.getName()));
        }

        return this;
    }

    public AgrestSwaggerModuleExtender entityPackage(Package aPackage) {
        agrestExtender().addBuilderCallback(b -> entityPackage(b, aPackage.getName()));
        return this;
    }

    private AgRuntimeBuilder entityPackage(AgRuntimeBuilder builder, String packageName) {
        return builder.module(AgSwaggerModule.builder().entityPackage(packageName).build());
    }

    private AgrestModuleExtender agrestExtender() {
        return agrestExtender != null ? agrestExtender : (agrestExtender = AgrestModule.extend(binder));
    }
}
