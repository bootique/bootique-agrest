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
