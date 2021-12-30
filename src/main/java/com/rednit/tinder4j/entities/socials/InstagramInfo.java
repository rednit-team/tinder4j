package com.rednit.tinder4j.entities.socials;

import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstagramInfo {

    private final String lastFetchTime;
    private final Boolean completedInitialFetch;
    private final int mediaCount;
    private final List<InstagramPhoto> photos;

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

    public String getLastFetchTime() {
        return lastFetchTime;
    }

    public Boolean getCompletedInitialFetch() {
        return completedInitialFetch;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public List<InstagramPhoto> getPhotos() {
        return photos;
    }

    public static class InstagramPhoto {

        private final String url;
        private final String thumbnail;
        private final String ts;

        public InstagramPhoto(DataObject photo) {
            this.url = photo.getString("image");
            this.thumbnail = photo.getString("thumbnail");
            this.ts = photo.getString("ts");
        }

        public String getUrl() {
            return url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getTs() {
            return ts;
        }
    }
}
