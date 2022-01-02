package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.photo.MatchPhoto;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchedUser extends GenericUser {

    private final String birthDateInfo;
    private final String lastOnline;
    private final boolean hideAge;
    private final boolean hideDistance;
    private final boolean isTravelling;
    private final List<MatchPhoto> photos;

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

    public String getBirthDateInfo() {
        return birthDateInfo;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public boolean isHideAge() {
        return hideAge;
    }

    public boolean isHideDistance() {
        return hideDistance;
    }

    public boolean isTravelling() {
        return isTravelling;
    }

    public List<MatchPhoto> getMatchPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return String.format("MatchedUser{id: %s / name: %s}", getId(), getName());
    }
}
