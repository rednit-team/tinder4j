package com.rednit.tinder4j.api.entities.user.swipeable;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.socials.Spotify;
import com.rednit.tinder4j.api.entities.user.GenericUser;
import com.rednit.tinder4j.api.entities.user.Metadata;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.async.RestActionImpl;
import com.rednit.tinder4j.requests.DataObject;
import com.rednit.tinder4j.requests.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract top level subtype of {@link GenericUser} for users the self user can swipe on.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see GenericUser
 * @since 1.0.0
 */
public abstract class SwipeableUser extends GenericUser {

    private final Metadata.Job job;
    private final String school;
    private final String city;
    private final int distance;
    private final long sNumber;
    private final List<Metadata.Teaser> teasers;
    private final List<String> interests;
    private final List<Metadata.Descriptor> descriptors;
    private final boolean showGenderOnProfile;
    private final List<Spotify.SpotifyTopArtist> topArtists;
    private final Spotify.SpotifyTrack themeTrack;

    /**
     * Constructs a new SwipeableUser.
     *
     * @param user   the {@link DataObject} to construct the SwipeableUser from
     * @param client the corresponding {@link TinderClient} instance
     */
    @SuppressWarnings("unchecked")
    protected SwipeableUser(DataObject user, TinderClient client) {
        super(user, client);
        if (!user.getArray("jobs").isEmpty()) {
            job = new Metadata.Job(user.getArray("jobs").getObject(0));
        } else {
            job = null;
        }
        if (!user.isNull("school")) {
            school = user.getArray("schools").getObject(0).getString("name");
        } else {
            school = null;
        }
        if (user.hasKey("city")) {
            city = user.getObject("city").getString("name");
        } else {
            city = null;
        }
        distance = user.getInteger("distance_mi");
        sNumber = user.getLong("s_number");
        teasers = new ArrayList<>();
        user.getArray("teasers").forEach(object ->
                teasers.add(new Metadata.Teaser(new DataObject((Map<String, Object>) object)))
        );
        interests = new ArrayList<>();
        if (user.hasKey("user_interests")) {
            user.getObject("user_interests").getArray("selected_interests").forEach(object ->
                    interests.add(new DataObject((Map<String, Object>) object).getString("name"))
            );
        }
        descriptors = new ArrayList<>();
        if (user.hasKey("selected_descriptors")) {
            user.getArray("selected_descriptors").forEach(object ->
                    descriptors.add(new Metadata.Descriptor(new DataObject((Map<String, Object>) object)))
            );
        }
        if (user.hasKey("show_gender_on_profile")) {
            showGenderOnProfile = user.getBoolean("show_gender_on_profile");
        } else {
            showGenderOnProfile = true; // assume it is true
        }
        topArtists = new ArrayList<>();
        if (user.hasKey("spotify_top_artists")) {
            user.getArray("spotify_top_artists").forEach(object ->
                    topArtists.add(new Spotify.SpotifyTopArtist(new DataObject((Map<String, Object>) object)))
            );
        }
        if (user.hasKey("spotify_theme_track")) {
            themeTrack = new Spotify.SpotifyTrack(user.getObject("spotify_theme_track"));
        } else {
            themeTrack = null;
        }
    }

    /**
     * Gets the {@link com.rednit.tinder4j.api.entities.user.Metadata.Job Job}.
     *
     * @return an {@link Optional} holding the {@link com.rednit.tinder4j.api.entities.user.Metadata.Job Job}.
     */
    public Optional<Metadata.Job> getJob() {
        return Optional.ofNullable(job);
    }

    /**
     * Gets the school.
     *
     * @return an {@link Optional} holding the school
     */
    public Optional<String> getSchool() {
        return Optional.ofNullable(school);
    }

    /**
     * Gets the city.
     *
     * @return an {@link Optional} holding the city
     */
    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    /**
     * Gets the distance as miles.
     *
     * @return the distance as miles
     */
    public int getDistanceMi() {
        return distance;
    }

    /**
     * Gets the distance as kilometers.
     *
     * @return the distance as kilometers
     */
    public float getDistanceKm() {
        return distance * 1.609344f;
    }

    /**
     * Gets the sNumber. <em>Developer note: I have no idea what the sNumber is.</em>
     *
     * @return the sNumber.
     */
    public long getSNumber() {
        return sNumber;
    }

    /**
     * Gets a possibly empty {@link List} of {@link com.rednit.tinder4j.api.entities.user.Metadata.Teaser Teasers}.
     *
     * @return a possibly empty {@link List} of {@link com.rednit.tinder4j.api.entities.user.Metadata.Teaser Teasers}
     */
    public List<Metadata.Teaser> getTeasers() {
        return teasers;
    }

    /**
     * Gets a possibly empty {@link List} of interests, such as reading, road trips, etc.
     *
     * @return a possibly empty {@link List} of interests
     */
    public List<String> getInterests() {
        return interests;
    }

    /**
     * Gets a possibly empty {@link List} of {@link com.rednit.tinder4j.api.entities.user.Metadata.Descriptor Descriptors}.
     *
     * @return a possibly empty {@link List} of
     * {@link com.rednit.tinder4j.api.entities.user.Metadata.Descriptor Descriptors}
     */
    public List<Metadata.Descriptor> getDescriptors() {
        return descriptors;
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
     * Gets a possibly empty {@link List} of
     * {@link com.rednit.tinder4j.api.entities.socials.Spotify.SpotifyTopArtist SpotifyTopArtists}.
     *
     * @return a possibly empty {@link List} of
     * {@link com.rednit.tinder4j.api.entities.socials.Spotify.SpotifyTopArtist SpotifyTopArtists}
     */
    public List<Spotify.SpotifyTopArtist> getTopArtists() {
        return topArtists;
    }

    /**
     * Gets the {@link com.rednit.tinder4j.api.entities.socials.Spotify.SpotifyTrack SpotifyThemeTrack}.
     *
     * @return an {@link Optional} holding the
     * {@link com.rednit.tinder4j.api.entities.socials.Spotify.SpotifyTrack SpotifyThemeTrack}
     */
    public Optional<Spotify.SpotifyTrack> getThemeTrack() {
        return Optional.ofNullable(themeTrack);
    }

    /**
     * Likes the user.
     *
     * @return {@link RestAction}
     */
    public RestAction<Void> like() {
        return new RestActionImpl<>(getClient(), Route.User.LIKE.compile(getId()));
    }

    /**
     * Dislikes/passes the user.
     *
     * @return {@link RestAction}
     */
    public RestAction<Void> dislike() {
        return new RestActionImpl<>(getClient(), Route.User.DISLIKE.compile(getId()));
    }

    /**
     * Superlikes the user. <em>Note: This is a Tinder Gold feature.</em>
     *
     * @return {@link RestAction}
     */
    public RestAction<Void> superlike() {
        return new RestActionImpl<>(getClient(), Route.User.SUPERLIKE.compile(getId()));
    }

    @Override
    public String toString() {
        return String.format("SwipeableUser{id: %s / name: %s}", getId(), getName());
    }
}
