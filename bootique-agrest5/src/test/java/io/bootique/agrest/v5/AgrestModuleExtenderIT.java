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
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Module;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class AgrestModuleExtenderIT {

    final JettyTester jetty = JettyTester.create();

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void moduleProvider() {

        TestModule module = new TestModule();
        TestProvider provider = new TestProvider(module);

        testFactory.app("-s")
                .autoLoadModules()
                .module(jetty.moduleReplacingConnectors())
                .module(b -> AgrestModule.extend(b).addModuleProvider(provider))
                .run();

        assertTrue(provider.moduleCalled);
        assertTrue(module.configureCalled);
    }

    @Test
    public void builderCallback() {

        TestCallback callback = new TestCallback();

        testFactory.app("-s")
                .autoLoadModules()
                .module(b -> AgrestModule.extend(b).addBuilderCallback(callback))
                .run();

        assertTrue(callback.configureCalled);
    }

    static class TestCallback implements AgBuilderCallback {
        boolean configureCalled;

        @Override
        public void configure(AgRuntimeBuilder builder) {
            this.configureCalled = true;
        }
    }

    static class TestModule implements Module {
        boolean configureCalled;

        @Override
        public void configure(Binder binder) {
            configureCalled = true;
        }
    }

    static class TestProvider implements AgModuleProvider {

        private final TestModule module;
        boolean moduleCalled;

        public TestProvider(TestModule module) {
            this.module = module;
        }

        @Override
        public Module module() {
            this.moduleCalled = true;
            return module;
        }

        @Override
        public Class<? extends Module> moduleType() {
            return TestModule.class;
        }
    }
}
