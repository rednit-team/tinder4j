package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.entities.photo.SizedImage;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Metadata {

    public static class Descriptor {

        private final String id;
        private final String name;
        private final String prompt;
        private final String iconUrl;
        private final List<SizedImage> iconUrls;
        private final String selection;

        @SuppressWarnings("unchecked")
        public Descriptor(DataObject descriptor) {
            id = descriptor.getString("id");
            name = descriptor.getString("name");
            prompt = descriptor.getString("prompt");
            iconUrl = descriptor.getString("icon_url");
            iconUrls = new ArrayList<>();
            descriptor.getArray("icon_urls").forEach(object ->
                    iconUrls.add(new SizedImage(new DataObject((Map<String, Object>) object)))
            );
            if (descriptor.hasKey("choice_selection")) {
                selection = descriptor.getString("choice_selection");
            } else {
                selection = null;
            }
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrompt() {
            return prompt;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public List<SizedImage> getIconUrls() {
            return iconUrls;
        }

        public Optional<String> getSelection() {
            return Optional.ofNullable(selection);
        }
    }

    public static class Job {

        private final String company;
        private final String title;

        public Job(DataObject job) {
            if (job.hasKey("company")) {
                company = job.getObject("company").getString("name");
            } else {
                company = null;
            }
            if (job.hasKey("title")) {
                title = job.getObject("title").getString("name");
            } else {
                title = null;
            }
        }

        public Optional<String> getCompany() {
            return Optional.ofNullable(company);
        }

        public Optional<String> getTitle() {
            return Optional.ofNullable(title);
        }
    }

    public static class Position {

        private final long at;
        private final double latitude;
        private final double longitude;

        public Position(DataObject position) {
            at = position.getLong("at");
            latitude = position.getDouble("lat");
            longitude = position.getDouble("lon");
        }

        public long getAt() {
            return at;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static class PositionInfo {

        private final String country;
        private final String cc;
        private final String alpha3;
        private final String timezone;

        public PositionInfo(DataObject position) {
            country = position.getObject("country").getString("name");
            cc =  position.getObject("country").getString("cc");
            alpha3 = position.getObject("country").getString("alpha3");
            timezone = position.getString("timezone");
        }

        public String getCountry() {
            return country;
        }

        public String getCc() {
            return cc;
        }

        public String getAlpha3() {
            return alpha3;
        }

        public String getTimezone() {
            return timezone;
        }
    }

    public static class Teaser {

        private final String type;
        private final String value;

        public Teaser(DataObject teaser) {
            type = teaser.getString("type");
            value = teaser.getString("value");
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

}
