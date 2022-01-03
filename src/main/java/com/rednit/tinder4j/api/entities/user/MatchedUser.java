package com.rednit.tinder4j.api.entities.user;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.photo.MatchPhoto;
import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Subtype of {@link GenericUser} for users the self user has a match with.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see GenericUser
 * @since 1.0.0
 */
public class MatchedUser extends GenericUser {

    private final String birthDateInfo;
    private final String lastOnline;
    private final boolean hideAge;
    private final boolean hideDistance;
    private final boolean isTravelling;
    private final List<MatchPhoto> photos;

    /**
     * Constructs a new MatchedUser.
     *
     * @param user   the {@link DataObject} to construct the MatchedUser from
     * @param client the corresponding {@link TinderClient} instance
     */
    @SuppressWarnings("unchecked")
    public MatchedUser(DataObject user, TinderClient client) {
        super(user, client);
        birthDateInfo = user.getString("birth_date_info");
        lastOnline = user.getString("ping_time");
        if (user.hasKey("hide_age")) {
            hideAge = user.getBoolean("hide_age");
        } else {
            hideAge = false;
        }
        if (user.hasKey("hide_distance")) {
            hideDistance = user.getBoolean("hide_distance");
        } else {
            hideDistance = false;
        }
        if (user.hasKey("is_travelling")) {
            isTravelling = user.getBoolean("is_travelling");
        } else {
            isTravelling = false;
        }
        photos = new ArrayList<>();
        user.getArray("photos").forEach(object ->
                photos.add(new MatchPhoto(new DataObject((Map<String, Object>) object), client))
        );
    }

    /**
     * Gets the birthdate information. The value of this field is always:
     * <em>fuzzy birth date active, not displaying real birth_date</em>.
     *
     * @return the birthdate information
     */
    public String getBirthDateInfo() {
        return birthDateInfo;
    }

    /**
     * Gets the last time the user was online.
     *
     * @return the last time the user was online
     */
    public String getLastOnline() {
        return lastOnline;
    }

    /**
     * Whether the age of the user should be hidden
     *
     * @return {@code true} if the age of the user should be hidden
     */
    public boolean hideAge() {
        return hideAge;
    }

    /**
     * Whether the distance of the user should be hidden
     *
     * @return {@code true} if the distance of the user should be hidden
     */
    public boolean hideDistance() {
        return hideDistance;
    }

    /**
     * Whether the user is travelling
     *
     * @return {@code true} if the user is travelling
     */
    public boolean isTravelling() {
        return isTravelling;
    }

    /**
     * Gets a {@link List} of {@link MatchPhoto MatchPhotos}. {@link MatchPhoto MatchPhotos} have more information
     * than {@link com.rednit.tinder4j.api.entities.photo.GenericPhoto GenericPhotos}.
     *
     * @return a {@link List} of {@link MatchPhoto MatchPhotos}
     */
    public List<MatchPhoto> getMatchPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return String.format("MatchedUser{id: %s / name: %s}", getId(), getName());
    }
}
