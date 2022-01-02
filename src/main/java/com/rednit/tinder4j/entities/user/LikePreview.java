package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.Entity;
import com.rednit.tinder4j.entities.photo.GenericPhoto;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LikePreview extends Entity {

    private final boolean recentlyActive;
    private final List<GenericPhoto> photos;

    @SuppressWarnings("unchecked")
    public LikePreview(DataObject entity, TinderClient client) {
        super(entity, client);
        photos = new ArrayList<>();
        entity.getArray("photos").forEach(object ->
                photos.add(new GenericPhoto(new DataObject((Map<String, Object>) object), client))
        );
        if (entity.hasKey("recently_active")) {
            recentlyActive = entity.getBoolean("recently_active");
        } else {
            recentlyActive = false;
        }
    }

    public List<GenericPhoto> getPhotos() {
        return photos;
    }

    public boolean isRecentlyActive() {
        return recentlyActive;
    }
}
