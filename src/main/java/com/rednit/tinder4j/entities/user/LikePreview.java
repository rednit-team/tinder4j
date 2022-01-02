package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.Entity;
import com.rednit.tinder4j.entities.photo.GenericPhoto;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A user that liked the self user.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class LikePreview extends Entity {

    private final boolean recentlyActive;
    private final List<GenericPhoto> photos;

    /**
     * Constructs a new LikePreview.
     *
     * @param entity the {@link DataObject} to construct the LikePreview from
     * @param client the corresponding {@link TinderClient} instance
     */
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

    /**
     * Gets a {@link List} of {@link GenericPhoto GenericPhotos} of the user.
     *
     * @return a {@link List} of {@link GenericPhoto GenericPhotos}
     */
    public List<GenericPhoto> getPhotos() {
        return photos;
    }

    /**
     * Whether the user was recently active.
     *
     * @return {@code true} if the user was recently active
     */
    public boolean isRecentlyActive() {
        return recentlyActive;
    }


    @Override
    public String toString() {
        return String.format("SelfUser{id: %s}", getId());
    }
}
