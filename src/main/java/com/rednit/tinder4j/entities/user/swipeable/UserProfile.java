package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserProfile extends SwipeableUser {

    private final List<String> sexualOrientations;
    private final String lastOnline;
    private final String birthDateInfo;
    private final boolean isTinderU;
    private final boolean hideDistance;
    private final boolean isTravelling;

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

    public List<String> getSexualOrientations() {
        return sexualOrientations;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public String getBirthDateInfo() {
        return birthDateInfo;
    }

    public boolean isTinderU() {
        return isTinderU;
    }

    public boolean isHideDistance() {
        return hideDistance;
    }

    public boolean isTravelling() {
        return isTravelling;
    }

    @Override
    public String toString() {
        return String.format("UserProfile{id: %s / name: %s}", getId(), getName());
    }
}
