package io.bootique.linkrest;

import com.nhl.link.rest.LrFeatureProvider;
import com.nhl.link.rest.LrModuleProvider;
import io.bootique.test.junit.BQTestFactory;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Injector;
import org.apache.cayenne.di.Module;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LinkRestModuleExtenderIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testFeatureProvider() {

        Feature feature = mock(Feature.class);
        LrFeatureProvider provider = mock(LrFeatureProvider.class);
        when(provider.feature(any(Injector.class))).thenReturn(feature);

        testFactory.app("-c", "classpath:LinkRestModuleExtenderIT.yml", "-s")
                .autoLoadModules()
                .module(b -> LinkRestModule.extend(b).addFeatureProvider(provider))
                .run();

        verify(provider).feature(any(Injector.class));
        verify(feature).configure(any(FeatureContext.class));
    }

    @Test
    public void testModuleProvider() {

        Module module = mock(Module.class);
        LrModuleProvider provider = mock(LrModuleProvider.class);
        when(provider.module()).thenReturn(module);

        testFactory.app("-c", "classpath:LinkRestModuleExtenderIT.yml", "-s")
                .autoLoadModules()
                .module(b -> LinkRestModule.extend(b).addModuleProvider(provider))
                .run();

        verify(provider).module();
        verify(module).configure(any(Binder.class));
    }
}
