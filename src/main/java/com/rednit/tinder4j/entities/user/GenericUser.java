package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.Entity;
import com.rednit.tinder4j.entities.photo.GenericPhoto;
import com.rednit.tinder4j.entities.user.swipeable.UserProfile;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.internal.requests.Route;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.*;

public abstract class GenericUser extends Entity {

    private final List<GenericPhoto> photos;
    private final String bio;
    private final String birthdate;
    private final String name;
    private final Gender gender;
    private final List<String> badges;

    @SuppressWarnings("unchecked")
    protected GenericUser(DataObject user, TinderClient client) {
        super(user, client);

        if (user.hasKey("bio")) {
            bio = user.getString("bio");
        } else {
            bio = "";
        }

        birthdate = user.getString("birth_date");
        name = user.getString("name");
        gender = Gender.valueOf(user.getString("gender").toUpperCase());

        badges = new ArrayList<>();
        user.getArray("badges").forEach(object ->
                badges.add(new DataObject((Map<String, Object>) object).getString("type"))
        );

        photos = new ArrayList<>();
        user.getArray("badges").forEach(object ->
                photos.add(new GenericPhoto(new DataObject((Map<String, Object>) object), client))
        );
    }

    public RestAction<UserProfile> getUserProfile() {
        return new RestActionImpl<>(getClient(), Route.User.GET_USER.compile(getId()), (response, request) ->
                new UserProfile(DataObject.fromJson(response.body()), getClient())
        );
    }

    public RestAction<Void> report(String cause, String text) {
        return new RestActionImpl<>(getClient(), Route.User.REPORT.compile(getId()), RequestBody.create(
                DataObject.empty().put("cause", cause).put("text", text).toString(),
                MediaType.parse("application/json")
        ));
    }

    public String getBio() {
        return bio;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getAge() {
        if (birthdate.length() < 4) {
            throw new UnsupportedOperationException("Birthdate is unavailable!");
        }
        int birthYear = Integer.parseInt(birthdate.substring(0, 4));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return year - birthYear;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean hasBadges() {
        return !badges.isEmpty();
    }

    public List<String> getBadges() {
        return badges;
    }

    public List<GenericPhoto> getPhotos() {
        return photos;
    }
}
