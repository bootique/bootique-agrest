package io.bootique.linkrest;

import com.nhl.link.rest.LrFeatureProvider;
import io.bootique.jetty.test.junit.JettyTestFactory;
import org.apache.cayenne.di.Injector;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LinkRestModuleExtenderIT {

    @Rule
    public JettyTestFactory testFactory = new JettyTestFactory();

    @Test
    public void testFeatureProvider() {

        Feature feature = mock(Feature.class);
        LrFeatureProvider featureProvider = mock(LrFeatureProvider.class);
        when(featureProvider.feature(any(Injector.class))).thenReturn(feature);

        testFactory.app("-c", "classpath:LinkRestModuleExtenderIT.yml")
                .autoLoadModules()
                .module(b -> LinkRestModule.extend(b).addFeatureProvider(featureProvider))
                .start();

        verify(featureProvider).feature(any(Injector.class));
        verify(feature).configure(any(FeatureContext.class));
    }
}
