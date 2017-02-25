package io.bootique.linkrest;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class LinkRestModuleProviderTest {

	@Test
	public void testPresentInJar() {
        BQModuleProviderChecker.testPresentInJar(LinkRestModuleProvider.class);
    }

	@Test
	public void testMetadata() {
		BQModuleProviderChecker.testMetadata(LinkRestModuleProvider.class);
	}
}
