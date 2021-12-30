package com.rednit.tinder4j.entities.socials;

import com.rednit.tinder4j.entities.photo.SizedImage;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spotify {

    public static class SpotifyAlbum {

        private final String id;
        private final String name;
        private final List<SizedImage> images;

        @SuppressWarnings("unchecked")
        public SpotifyAlbum(DataObject album) {
            id = album.getString("id");
            name = album.getString("name");
            images = new ArrayList<>();
            album.getArray("processedFiles").forEach(object ->
                    images.add(new SizedImage(new DataObject((Map<String, Object>) object)))
            );
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<SizedImage> getImages() {
            return images;
        }
    }

    public static class SpotifyArtist {

        private final String id;
        private final String name;

        public SpotifyArtist(DataObject artist) {
            id = artist.getString("id");
            name = artist.getString("name");
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class SpotifySongAttachment {

        private final SpotifyAlbum album;
        private final List<SpotifyArtist> artists;
        private final String url;

        @SuppressWarnings("unchecked")
        public SpotifySongAttachment(DataObject attachment) {
            album = new SpotifyAlbum(attachment.getObject("album"));
            artists = new ArrayList<>();
            attachment.getArray("artists").forEach(object ->
                artists.add(new SpotifyArtist(new DataObject((Map<String, Object>) object)))
            );
            url = attachment.getString("url");
        }

        public SpotifyAlbum getAlbum() {
            return album;
        }

        public List<SpotifyArtist> getArtists() {
            return artists;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class SpotifyTrack extends SpotifySongAttachment {

        private final String uri;

        public SpotifyTrack(DataObject track) {
            super(track);
            uri = track.getString("uri");
        }

        public String getUri() {
            return uri;
        }
    }

    public static class SpotifyTopArtist {

        private final boolean selected;
        private final SpotifyTrack track;

        public SpotifyTopArtist(DataObject artist) {
            selected = artist.getBoolean("selected");
            track = new SpotifyTrack(artist.getObject("top_track"));
        }

        public boolean isSelected() {
            return selected;
        }

        public SpotifyTrack getTrack() {
            return track;
        }
    }
}
