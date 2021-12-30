package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.photo.ProfilePhoto;
import com.rednit.tinder4j.entities.socials.InstagramInfo;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.*;

public class SelfUser extends GenericUser {

    private final int ageFilterMin;
    private final int ageFilterMax;
    private final String createDate;
    private final int distanceFilter;
    private final Gender genderFilter;
    private final String email;
    private final InstagramInfo instagramInfo;
    private final Set<Gender> interestedIn;
    private final Metadata.Job job;
    private final boolean photoOptimizerEnabled;
    private final String lastOnline;
    private final Metadata.Position position;
    private final Metadata.PositionInfo positionInfo;
    private final String school;
    private final boolean showGenderOnProfile;
    private final boolean canCreateSquad;
    private final List<ProfilePhoto> photos;

    @SuppressWarnings("unchecked")
    public SelfUser(DataObject user, TinderClient client) {
        super(user, client);
        ageFilterMin = user.getInteger("age_filter_min");
        ageFilterMax = user.getInteger("age_filter_max");
        createDate = user.getString("create_date");
        distanceFilter = user.getInteger("distance_filter");
        genderFilter = Gender.values()[user.getInteger("gender_filter")];
        email = user.getString("email");

        if (user.hasKey("instagram")) {
            instagramInfo = new InstagramInfo(user.getObject("instagram"));
        } else {
            instagramInfo = null;
        }

        interestedIn = new HashSet<>();
        for (int gender : user.get("interested_in", int[].class)) {
            interestedIn.add(Gender.values()[gender]);
        }

        if (user.hasKey("jobs")) {
            job = new Metadata.Job(user.getObject("jobs"));
        } else {
            job = null;
        }

        photoOptimizerEnabled = user.getBoolean("photo_optimizer_enabled");
        lastOnline = user.getString("ping_time");
        position = new Metadata.Position(user.getObject("pos"));
        positionInfo = new Metadata.PositionInfo(user.getObject("pos_info"));

        if (user.hasKey("school")) {
            school = user.getArray("schools").getObject(0).getString("name");
        } else {
            school = null;
        }

        showGenderOnProfile = user.getBoolean("show_gender_on_profile");
        canCreateSquad = user.getBoolean("can_create_squad");
        photos = new ArrayList<>();
        user.getArray("badges").forEach(object ->
                photos.add(new ProfilePhoto(new DataObject((Map<String, Object>) object), client))
        );
    }

    public int getAgeFilterMin() {
        return ageFilterMin;
    }

    public int getAgeFilterMax() {
        return ageFilterMax;
    }

    public String getCreateDate() {
        return createDate;
    }

    public int getDistanceFilter() {
        return distanceFilter;
    }

    public Gender getGenderFilter() {
        return genderFilter;
    }

    public String getEmail() {
        return email;
    }

    public Optional<InstagramInfo> getInstagramInfo() {
        return Optional.ofNullable(instagramInfo);
    }

    public Set<Gender> getInterestedIn() {
        return interestedIn;
    }

    public Optional<Metadata.Job> getJob() {
        return Optional.ofNullable(job);
    }

    public boolean isPhotoOptimizerEnabled() {
        return photoOptimizerEnabled;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public Metadata.Position getPosition() {
        return position;
    }

    public Metadata.PositionInfo getPositionInfo() {
        return positionInfo;
    }

    public Optional<String> getSchool() {
        return Optional.ofNullable(school);
    }

    public boolean isShowGenderOnProfile() {
        return showGenderOnProfile;
    }

    public boolean isCanCreateSquad() {
        return canCreateSquad;
    }

    public List<ProfilePhoto> getProfilePhotos() {
        return photos;
    }

    @Override
    public RestAction<Void> report(String cause, String text) {
        throw new UnsupportedOperationException("You cannot report yourself!");
    }

    public RestAction<Void> updateJob(Metadata.Job job) {
        return null;
    }

    public RestAction<Void> removeJob() {
        return null;
    }

    public RestAction<Void> modifyBio(String bio) {
        return null;
    }

    public RestAction<Void> modifySchool(String school) {
        return null;
    }

    public RestAction<Void> updateCity(DataObject city) {
        return null;
    }

    public RestAction<Void> removeCity() {
        return null;
    }

    public RestAction<Void> modifyGender(Gender gender, boolean showGenderOnProfile) {
        return null;
    }

    public RestAction<Void> modifySearchPreferences(int ageFilterMin,
                                                    int ageFilterMax,
                                                    Gender genderFilter,
                                                    Gender gender,
                                                    int distanceFilter) {
        return null;
    }

}
