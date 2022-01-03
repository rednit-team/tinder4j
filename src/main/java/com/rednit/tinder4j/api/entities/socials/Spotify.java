package com.rednit.tinder4j.api.entities.socials;

import com.rednit.tinder4j.api.entities.photo.SizedImage;
import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains all wrapped Spotify models.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class Spotify {

    /**
     * An album on Spotify.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class SpotifyAlbum {

        private final String id;
        private final String name;
        private final List<SizedImage> images;

        /**
         * Constructs a new SpotifyAlbum.
         *
         * @param album the {@link DataObject} to construct the SpotifyAlbum from
         */
        @SuppressWarnings("unchecked")
        public SpotifyAlbum(DataObject album) {
            id = album.getString("id");
            name = album.getString("name");
            images = new ArrayList<>();
            album.getArray("images").forEach(object ->
                    images.add(new SizedImage(new DataObject((Map<String, Object>) object)))
            );
        }

        /**
         * Gets the id of the album.
         *
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the name of the album.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns a {@link List} of {@link SizedImage SizedImages} containing the album cover in different resolutions.
         *
         * @return a {@link List} of {@link SizedImage SizedImages}
         */
        public List<SizedImage> getImages() {
            return images;
        }
    }

    /**
     * An artist on Spotify.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class SpotifyArtist {

        private final String id;
        private final String name;

        /**
         * Constructs a new SpotifyArtist.
         *
         * @param artist the {@link DataObject} to construct the SpotifyArtist from
         */
        public SpotifyArtist(DataObject artist) {
            id = artist.getString("id");
            name = artist.getString("name");
        }

        /**
         * Gets the id of the artist.
         *
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the name of the artist.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * A Spotify song attached to a chat message.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see com.rednit.tinder4j.api.entities.message.Attachment.SongAttachment SongAttachment
     * @since 1.0.0
     */
    public static class SpotifySongAttachment {

        private final SpotifyAlbum album;
        private final List<SpotifyArtist> artists;
        private final String url;

        /**
         * Constructs a new SpotifySongAttachment.
         *
         * @param attachment the {@link DataObject} to construct the SpotifySongAttachment from
         */
        @SuppressWarnings("unchecked")
        public SpotifySongAttachment(DataObject attachment) {
            album = new SpotifyAlbum(attachment.getObject("album"));
            artists = new ArrayList<>();
            attachment.getArray("artists").forEach(object ->
                    artists.add(new SpotifyArtist(new DataObject((Map<String, Object>) object)))
            );
            url = attachment.getString("url");
        }

        /**
         * Gets the {@link SpotifyAlbum}
         *
         * @return the {@link SpotifyAlbum}
         */
        public SpotifyAlbum getAlbum() {
            return album;
        }

        /**
         * Gets a {@link List} of all {@link SpotifyArtist SpotifyArtists} featured on this track.
         *
         * @return a {@link List} of {@link SpotifyArtist SpotifyArtists}
         */
        public List<SpotifyArtist> getArtists() {
            return artists;
        }

        /**
         * Gets the url to the song.
         *
         * @return the url to the song
         */
        public String getUrl() {
            return url;
        }
    }

    /**
     * Subtype of {@link SpotifySongAttachment} used in various places inside {@link Spotify}.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see com.rednit.tinder4j.api.entities.message.Attachment.SongAttachment SongAttachment
     * @see Spotify
     * @since 1.0.0
     */
    public static class SpotifyTrack extends SpotifySongAttachment {

        private final String uri;

        /**
         * Constructs a new SpotifyTrack.
         *
         * @param track the {@link DataObject} to construct the SpotifyTrack from
         */
        public SpotifyTrack(DataObject track) {
            super(track);
            uri = track.getString("uri");
        }

        /**
         * Gets the uri to the song.
         *
         * @return the uri to the song
         */
        public String getUri() {
            return uri;
        }
    }

    /**
     * A Spotify artist used inside the top artists of a
     * {@link com.rednit.tinder4j.api.entities.user.swipeable.UserProfile UserProfile}.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see com.rednit.tinder4j.api.entities.user.swipeable.UserProfile UserProfile
     * @since 1.0.0
     */
    public static class SpotifyTopArtist {

        private final boolean selected;
        private final SpotifyTrack track;

        /**
         * Constructs a new SpotifyTopArtist.
         *
         * @param artist the {@link DataObject} to construct the SpotifyTopArtist from
         */
        public SpotifyTopArtist(DataObject artist) {
            selected = artist.getBoolean("selected");
            track = new SpotifyTrack(artist.getObject("top_track"));
        }

        /**
         * Whether this artist is selected as top artist
         *
         * @return {@code true} if this artist is selected as top artist
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Gets the most popular track of the artist
         *
         * @return a {@link SpotifyTrack}
         */
        public SpotifyTrack getTrack() {
            return track;
        }
    }
}
