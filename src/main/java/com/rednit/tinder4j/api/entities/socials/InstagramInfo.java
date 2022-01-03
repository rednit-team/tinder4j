package com.rednit.tinder4j.api.entities.socials;

import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class containing Instagram photos if the Tinder profile is connected with Instagram.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class InstagramInfo {

    private final String lastFetchTime;
    private final Boolean completedInitialFetch;
    private final int mediaCount;
    private final List<InstagramPhoto> photos;

    /**
     * Constructs a new InstagramInfo
     *
     * @param info the {@link DataObject} to construct the InstagramInfo from
     */
    @SuppressWarnings("unchecked")
    public InstagramInfo(DataObject info) {
        lastFetchTime = info.getString("last_fetch_time");
        completedInitialFetch = info.getBoolean("completed_initial_fetch");
        mediaCount = info.getInteger("media_count");
        photos = new ArrayList<>();
        info.getArray("processedFiles").forEach(object ->
                photos.add(new InstagramPhoto(new DataObject((Map<String, Object>) object)))
        );
    }

    /**
     * Gets the last time Instagram got fetched.
     *
     * @return the last fetch time
     */
    public String getLastFetchTime() {
        return lastFetchTime;
    }

    /**
     * Whether the initial fetch got completed. For instance, this can be {@code false} if the user aborted the
     * Instagram authentication.
     *
     * @return {@code true} if the initial fetch got completed
     */
    public Boolean completedInitialFetch() {
        return completedInitialFetch;
    }

    /**
     * Gets the media count.
     *
     * @return the media count
     */
    public int getMediaCount() {
        return mediaCount;
    }

    /**
     * A possibly empty {@link List} of {@link InstagramPhoto InstagramPhotos}.
     *
     * @return a possibly empty {@link List} of {@link InstagramPhoto InstagramPhotos}
     */
    public List<InstagramPhoto> getPhotos() {
        return photos;
    }

    /**
     * A photo from Instagram, only used inside a {@link InstagramInfo}.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see InstagramInfo
     * @since 1.0.0
     */
    public static class InstagramPhoto {

        private final String url;
        private final String thumbnail;
        private final String ts;

        /**
         * Constructs a new InstagramPhoto.
         *
         * @param photo the {@link DataObject} to construct the InstagramPhoto from
         */
        public InstagramPhoto(DataObject photo) {
            this.url = photo.getString("image");
            this.thumbnail = photo.getString("thumbnail");
            this.ts = photo.getString("ts");
        }

        /**
         * Gets the image url, uses the Instagram cdn.
         *
         * @return the image url
         */
        public String getUrl() {
            return url;
        }

        /**
         * Gets the thumbnail image url, uses the Instagram cdn.
         *
         * @return the image url
         */
        public String getThumbnail() {
            return thumbnail;
        }

        public String getTs() {
            return ts;
        }
    }
}
