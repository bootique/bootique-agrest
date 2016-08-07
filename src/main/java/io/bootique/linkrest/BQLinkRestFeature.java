package io.bootique.linkrest;

import com.google.inject.Inject;
import com.nhl.link.rest.runtime.LinkRestRuntime;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * An injectable wrapper around {@link LinkRestRuntime} feature.
 */
public class BQLinkRestFeature implements Feature {

	private LinkRestRuntime lrRuntime;

	@Inject
	public BQLinkRestFeature(LinkRestRuntime lrRuntime) {
		this.lrRuntime = lrRuntime;
	}

	@Override
	public boolean configure(FeatureContext context) {
		lrRuntime.configure(context);
		return true;
	}
}
