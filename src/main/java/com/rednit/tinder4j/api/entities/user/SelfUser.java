package com.rednit.tinder4j.api.entities.user;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.photo.ProfilePhoto;
import com.rednit.tinder4j.api.entities.socials.InstagramInfo;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.DataObject;

import java.util.*;

/**
 * Subtype of {@link GenericUser} describing the self user.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see GenericUser
 * @since 1.0.0
 */
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

    /**
     * Constructs a new SelfUser.
     *
     * @param user   the {@link DataObject} to construct the SelfUser from
     * @param client the corresponding {@link TinderClient} instance
     */
    @SuppressWarnings("unchecked")
    public SelfUser(DataObject user, TinderClient client) {
        super(user, client);
        ageFilterMin = user.getInteger("age_filter_min");
        ageFilterMax = user.getInteger("age_filter_max");
        createDate = user.getString("create_date");
        distanceFilter = user.getInteger("distance_filter");
        genderFilter = Gender.fromId(user.getInteger("gender_filter"));
        email = user.getString("email");

        if (user.hasKey("instagram")) {
            instagramInfo = new InstagramInfo(user.getObject("instagram"));
        } else {
            instagramInfo = null;
        }

        interestedIn = new HashSet<>();
        for (Object gender : user.get("interested_in", List.class)) {
            interestedIn.add(Gender.fromId((int) gender));
        }

        if (!user.getArray("jobs").isEmpty()) {
            job = new Metadata.Job(user.getArray("jobs").getObject(0));
        } else {
            job = null;
        }

        photoOptimizerEnabled = user.getBoolean("photo_optimizer_enabled");
        lastOnline = user.getString("ping_time");

        if (!user.isNull("pos")) {
            position = new Metadata.Position(user.getObject("pos"));
        } else {
            position = null;
        }

        positionInfo = new Metadata.PositionInfo(user.getObject("pos_info"));

        if (!user.isNull("school")) {
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

    /**
     * Gets the minimum age filter.
     *
     * @return the minimum age filter
     */
    public int getAgeFilterMin() {
        return ageFilterMin;
    }

    /**
     * Gets the maximum age filter.
     *
     * @return the maximum age filter
     */
    public int getAgeFilterMax() {
        return ageFilterMax;
    }

    /**
     * Gets the date the profile was created.
     *
     * @return the date the profile was created
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Gets the distance filter.
     *
     * @return the distance filter
     */
    public int getDistanceFilter() {
        return distanceFilter;
    }

    /**
     * Gets the {@link Gender} filter.
     *
     * @return the {@link Gender} filter
     */
    public Gender getGenderFilter() {
        return genderFilter;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * If connected, gets the {@link InstagramInfo}, else {@link Optional#empty()}.
     *
     * @return an {@link Optional} holding the {@link InstagramInfo}
     */
    public Optional<InstagramInfo> getInstagramInfo() {
        return Optional.ofNullable(instagramInfo);
    }

    /**
     * Gets a {@link Set} of {@link Gender Genders}.
     *
     * @return a {@link Set} of {@link Gender Genders}
     */
    public Set<Gender> getInterestedIn() {
        return interestedIn;
    }

    /**
     * Gets the {@link com.rednit.tinder4j.api.entities.user.Metadata.Job Job}.
     *
     * @return an {@link Optional} holding the {@link com.rednit.tinder4j.api.entities.user.Metadata.Job Job}
     */
    public Optional<Metadata.Job> getJob() {
        return Optional.ofNullable(job);
    }

    /**
     * Whether the photo optimizer is enabled.
     * <em>Photo optimizer regularly scans the users' gallery for possible profile photos.</em>
     *
     * @return {@code true} if the photo optimizer is enabled
     */
    public boolean isPhotoOptimizerEnabled() {
        return photoOptimizerEnabled;
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
     * Gets the {@link com.rednit.tinder4j.api.entities.user.Metadata.Position Position}.
     *
     * @return an {@link Optional} holding the {@link com.rednit.tinder4j.api.entities.user.Metadata.Position Position}
     */
    public Optional<Metadata.Position> getPosition() {
        return Optional.ofNullable(position);
    }

    /**
     * Gets the {@link com.rednit.tinder4j.api.entities.user.Metadata.PositionInfo PositionInfo}.
     *
     * @return the {@link com.rednit.tinder4j.api.entities.user.Metadata.PositionInfo PositionInfo}
     */
    public Metadata.PositionInfo getPositionInfo() {
        return positionInfo;
    }

    /**
     * Gets the school.
     *
     * @return an {@link Optional} holding the school.
     */
    public Optional<String> getSchool() {
        return Optional.ofNullable(school);
    }

    /**
     * Whether to show the gender on profile.
     *
     * @return {@code true} if the gender should be shown
     */
    public boolean showGenderOnProfile() {
        return showGenderOnProfile;
    }

    /**
     * Whether the user can create a squad. <em>Developer note: I have no idea what a squad is in Tinder context.</em>
     *
     * @return {@code true} if the user can create a squad
     */
    public boolean canCreateSquad() {
        return canCreateSquad;
    }

    /**
     * Gets a {@link List} of {@link ProfilePhoto ProfilePhotos}. {@link ProfilePhoto ProfilePhotos} have more
     * information than {@link com.rednit.tinder4j.api.entities.photo.GenericPhoto GenericPhotos}.
     *
     * @return a {@link List} of {@link ProfilePhoto ProfilePhotos}
     */
    public List<ProfilePhoto> getProfilePhotos() {
        return photos;
    }

    /**
     * @throws UnsupportedOperationException if you try to report yourself
     */
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

    @Override
    public String toString() {
        return String.format("SelfUser{id: %s / name: %s}", getId(), getName());
    }
}
