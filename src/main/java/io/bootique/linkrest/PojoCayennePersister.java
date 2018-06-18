/**
 *  Licensed to ObjectStyle LLC under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ObjectStyle LLC licenses
 *  this file to you under the Apache License, Version 2.0 (the
 *  “License”); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.bootique.linkrest;

import com.nhl.link.rest.runtime.cayenne.ICayennePersister;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.map.EntityResolver;

/**
 * Stub Link Rest persistence service, intended for use when Cayenne is not present in the app.
 * Used automatically by Bootique Link Rest module.
 */
class PojoCayennePersister implements ICayennePersister {

    private EntityResolver entityResolver;

    PojoCayennePersister() {
        this.entityResolver = new EntityResolver();
    }

    @Override
    public ObjectContext sharedContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectContext newContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityResolver entityResolver() {
        return entityResolver;
    }
}
