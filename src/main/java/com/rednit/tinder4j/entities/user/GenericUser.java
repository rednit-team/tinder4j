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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Abstract top level class for users.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class GenericUser extends Entity {

    private final List<GenericPhoto> photos;
    private final String bio;
    private final String birthdate;
    private final String name;
    private final Gender gender;
    private final List<String> badges;

    /**
     * Constructs a new GenericUser.
     *
     * @param user   the {@link DataObject} to construct the GenericUser from
     * @param client the corresponding {@link TinderClient} instance
     */
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
        gender = Gender.fromId(user.getInteger("gender"));

        badges = new ArrayList<>();
        if (user.hasKey("badges")) {
            user.getArray("badges").forEach(object ->
                    badges.add(new DataObject((Map<String, Object>) object).getString("type"))
            );
        }

        photos = new ArrayList<>();
        user.getArray("photos").forEach(object ->
                photos.add(new GenericPhoto(new DataObject((Map<String, Object>) object), client))
        );
    }

    /**
     * Gets the {@link UserProfile}. A {@link UserProfile} contains all available information about a user, while this
     * class and its subtypes may only contain certain information.
     *
     * @return the {@link UserProfile} wrapped in a {@link RestAction}
     */
    public RestAction<UserProfile> getUserProfile() {
        return new RestActionImpl<>(getClient(), Route.User.GET_USER.compile(getId()), (response, request) ->
                new UserProfile(DataObject.fromJson(response.body()).getObject("results"), getClient())
        );
    }

    /**
     * Reports the user.
     *
     * @param cause the report cause
     * @param text  the detailed report text
     * @return {@link RestAction}
     */
    public RestAction<Void> report(String cause, String text) {
        return new RestActionImpl<>(getClient(), Route.User.REPORT.compile(getId()), RequestBody.create(
                DataObject.empty().put("cause", cause).put("text", text).toString(),
                MediaType.parse("application/json")
        ));
    }

    /**
     * Gets the bio of the user.
     *
     * @return the bio of the user
     */
    public String getBio() {
        return bio;
    }

    /**
     * Gets the users' birthdate. <em>Note:</em> This does only display the real birthdate if it's the {@link SelfUser}.
     *
     * @return the users' birthdate
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Gets the age of the user. Uses {@link GenericUser#getBirthdate()} to calculate the age.
     *
     * @return the age of the user
     * @throws UnsupportedOperationException if the birthdate is not available
     */
    public int getAge() {
        if (birthdate.length() < 4) {
            throw new UnsupportedOperationException("Birthdate is unavailable!");
        }
        int birthYear = Integer.parseInt(birthdate.substring(0, 4));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return year - birthYear - 1; // I don't understand Tinder's age calculation, but -1 seems to fix my solution
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the {@link Gender} of the user.
     *
     * @return the {@link Gender} of the user.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Whether this user has badges.
     *
     * @return {@code true} if this user has badges
     * @see GenericUser#getBadges() ()
     */
    public boolean hasBadges() {
        return !badges.isEmpty();
    }

    /**
     * Gets a possibly empty {@link List} the badges the user has.
     *
     * @return a possibly empty {@link List} the badges
     */
    public List<String> getBadges() {
        return badges;
    }

    /**
     * Gets a {@link List} of {@link GenericPhoto GenericPhotos} of the user.
     *
     * @return a {@link List} of {@link GenericPhoto GenericPhotos}
     */
    public List<GenericPhoto> getPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return String.format("GenericUser{id: %s / name: %s}", getId(), getName());
    }
}
