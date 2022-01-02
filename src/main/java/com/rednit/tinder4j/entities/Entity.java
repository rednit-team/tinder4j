package com.rednit.tinder4j.entities;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

/**
 * Abstract top level class for all Tinder entities that have an id. This limits to ids that can be used as url
 * parameters. In other words, this class only applies to entities, such as users, matches, etc. and not spotify songs,
 * descriptors or similar.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Entity {

    private final String id;
    private final TinderClient client;

    /**
     * Constructs a new Entity.
     *
     * @param entity the {@link DataObject} to construct the Entity from
     * @param client the corresponding {@link TinderClient} instance
     * @throws IllegalArgumentException if the entity doesn't have an id field
     */
    protected Entity(DataObject entity, TinderClient client) {
        if (entity.hasKey("id")) {
            id = entity.getString("id");
        } else if (entity.hasKey("_id")) {
            id = entity.getString("_id");
        } else {
            throw new IllegalArgumentException("Not an entity!");
        }
        this.client = client;
    }

    /**
     * Gets the id of the entity.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the corresponding {@link TinderClient} instance.
     *
     * @return the {@link TinderClient} instance
     */
    public TinderClient getClient() {
        return client;
    }

    @Override
    public String toString() {
        return String.format("Entity{id: %s}", id);
    }
}
