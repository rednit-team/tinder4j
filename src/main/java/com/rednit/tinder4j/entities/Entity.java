package com.rednit.tinder4j.entities;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

public abstract class Entity {

    private final String id;
    private final TinderClient client;

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

    public String getId() {
        return id;
    }

    public TinderClient getClient() {
        return client;
    }

    @Override
    public String toString() {
        return String.format("Entity{id: %s}", id);
    }
}
