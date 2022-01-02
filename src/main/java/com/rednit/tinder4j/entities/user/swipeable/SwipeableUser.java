package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.socials.Spotify;
import com.rednit.tinder4j.entities.user.GenericUser;
import com.rednit.tinder4j.entities.user.Metadata;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.internal.requests.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Metadata.Job> getJob() {
        return Optional.ofNullable(job);
    }

    public Optional<String> getSchool() {
        return Optional.ofNullable(school);
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public int getDistanceMi() {
        return distance;
    }

    public float getDistanceKm() {
        return distance * 1.609344f;
    }

    public long getsNumber() {
        return sNumber;
    }

    public List<Metadata.Teaser> getTeasers() {
        return teasers;
    }

    public List<String> getInterests() {
        return interests;
    }

    public List<Metadata.Descriptor> getDescriptors() {
        return descriptors;
    }

    public boolean isShowGenderOnProfile() {
        return showGenderOnProfile;
    }

    public List<Spotify.SpotifyTopArtist> getTopArtists() {
        return topArtists;
    }

    public Optional<Spotify.SpotifyTrack> getThemeTrack() {
        return Optional.ofNullable(themeTrack);
    }

    public RestAction<Void> like() {
        return new RestActionImpl<>(getClient(), Route.User.LIKE.compile(getId()));
    }

    public RestAction<Void> dislike() {
        return new RestActionImpl<>(getClient(), Route.User.DISLIKE.compile(getId()));
    }

    public RestAction<Void> superlike() {
        return new RestActionImpl<>(getClient(), Route.User.SUPERLIKE.compile(getId()));
    }

    @Override
    public String toString() {
        return String.format("SwipeableUser{id: %s / name: %s}", getId(), getName());
    }
}
