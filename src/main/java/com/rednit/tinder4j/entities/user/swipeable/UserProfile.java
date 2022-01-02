package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Subtype of {@link SwipeableUser} for a complete user profile.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see SwipeableUser
 * @since 1.0.0
 */
public class UserProfile extends SwipeableUser {

    private final List<String> sexualOrientations;
    private final String lastOnline;
    private final String birthDateInfo;
    private final boolean isTinderU;
    private final boolean hideDistance;
    private final boolean isTravelling;

    /**
     * Constructs a new UserProfile.
     *
     * @param user   the {@link DataObject} to construct the UserProfile from
     * @param client the corresponding {@link TinderClient} instance
     */
    @SuppressWarnings("unchecked")
    public UserProfile(DataObject user, TinderClient client) {
        super(user, client);
        sexualOrientations = new ArrayList<>();
        if (user.hasKey("sexual_orientations")) {
            user.getArray("sexual_orientations").forEach(object ->
                    sexualOrientations.add(new DataObject((Map<String, Object>) object).getString("name"))
            );
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
        isTinderU = user.getBoolean("is_tinder_u");
        birthDateInfo = user.getString("birth_date_info");
        lastOnline = user.getString("ping_time");
    }

    /**
     * Gets a possibly empty {@link List} of the sexual orientations of a user.
     *
     * @return a possibly empty {@link List} of the sexual orientations
     */
    public List<String> getSexualOrientations() {
        return sexualOrientations;
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
     * Gets the birthdate information. The value of this field is always:
     * <em>fuzzy birth date active, not displaying real birth_date</em>.
     *
     * @return the birthdate information
     */
    public String getBirthDateInfo() {
        return birthDateInfo;
    }

    /**
     * Whether the user uses Tinder University.
     *
     * @return {@code true} if the user uses Tinder University
     */
    public boolean isTinderU() {
        return isTinderU;
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

    @Override
    public String toString() {
        return String.format("UserProfile{id: %s / name: %s}", getId(), getName());
    }
}
