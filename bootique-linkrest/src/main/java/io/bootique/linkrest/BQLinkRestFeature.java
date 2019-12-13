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

package io.bootique.linkrest;

import com.nhl.link.rest.runtime.LinkRestRuntime;

import javax.inject.Inject;
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
