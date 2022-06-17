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
import io.agrest.runtime.AgRuntimeBuilder;
import io.bootique.agrest.v5.AgBuilderCallback;
import io.bootique.agrest.v5.AgrestModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Module;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@BQTest
public class AgrestModuleExtenderIT {

    final JettyTester jetty = JettyTester.create();

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testModuleProvider() {

        Module module = mock(Module.class);
        AgModuleProvider provider = mock(AgModuleProvider.class);
        when(provider.module()).thenReturn(module);

        testFactory.app("-s")
                .autoLoadModules()
                .module(jetty.moduleReplacingConnectors())
                .module(b -> AgrestModule.extend(b).addModuleProvider(provider))
                .run();

        verify(provider).module();
        verify(module).configure(any(Binder.class));
    }

    @Test
    public void testBuilderCallback() {

        AgBuilderCallback callback = mock(AgBuilderCallback.class);

        testFactory.app("-s")
                .autoLoadModules()
                .module(b -> AgrestModule.extend(b).addBuilderCallback(callback))
                .run();

        verify(callback).configure(any(AgRuntimeBuilder.class));
    }
}
