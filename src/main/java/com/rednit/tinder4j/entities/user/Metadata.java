package com.rednit.tinder4j.entities.user;

import com.rednit.tinder4j.entities.photo.SizedImage;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class contains all wrapped user metadata models.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class Metadata {

    /**
     * A profile descriptor such as hobbies, etc.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Descriptor {

        private final String id;
        private final String name;
        private final String prompt;
        private final String iconUrl;
        private final List<SizedImage> iconUrls;
        private final String selection;

        /**
         * Constructs a new Descriptor.
         *
         * @param descriptor the {@link DataObject} to construct the Descriptor from
         */
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

        /**
         * Gets the id of the descriptor.
         *
         * @return the id of the descriptor
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the name of the descriptor.
         *
         * @return the name of the descriptor
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the prompt of the descriptor.
         *
         * @return the prompt of the descriptor
         */
        public String getPrompt() {
            return prompt;
        }

        /**
         * Gets the icon url of the descriptor.
         *
         * @return the icon url of the descriptor
         */
        public String getIconUrl() {
            return iconUrl;
        }

        /**
         * Gets a {@link List} of icon urls.
         *
         * @return a {@link List} of icon urls
         */
        public List<SizedImage> getIconUrls() {
            return iconUrls;
        }

        /**
         * Gets the descriptor choice the user made.
         *
         * @return an {@link Optional} holding the descriptor choice the user made
         */
        public Optional<String> getSelection() {
            return Optional.ofNullable(selection);
        }
    }

    /**
     * A Job. Jobs consist of a title and a company.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Job {

        private final String company;
        private final String title;

        /**
         * Constructs a new Job.
         *
         * @param job the {@link DataObject} to construct the Job from
         */
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

        /**
         * Gets the company.
         *
         * @return an {@link Optional} holding the company
         */
        public Optional<String> getCompany() {
            return Optional.ofNullable(company);
        }

        /**
         * Gets the title.
         *
         * @return an {@link Optional} holding the title
         */
        public Optional<String> getTitle() {
            return Optional.ofNullable(title);
        }
    }

    /**
     * Position information, used inside the {@link SelfUser}.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see SelfUser
     * @since 1.0.0
     */
    public static class Position {

        private final long at;
        private final double latitude;
        private final double longitude;

        /**
         * Constructs a new Position.
         *
         * @param position the {@link DataObject} to construct the Position from
         */
        public Position(DataObject position) {
            at = position.getLong("at");
            latitude = position.getDouble("lat");
            longitude = position.getDouble("lon");
        }

        /**
         * I have no idea.
         *
         * @return a long
         */
        public long getAt() {
            return at;
        }

        /**
         * Gets the latitude.
         *
         * @return the latitude
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Gets the longitude.
         *
         * @return the longitude
         */
        public double getLongitude() {
            return longitude;
        }
    }

    /**
     * Information about the country, used inside the {@link SelfUser}.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see SelfUser
     * @since 1.0.0
     */
    public static class PositionInfo {

        private final String country;
        private final String cc;
        private final String alpha3;
        private final String timezone;

        /**
         * Constructs a new PositionInfo.
         *
         * @param position the {@link DataObject} to construct the PositionInfo from
         */
        public PositionInfo(DataObject position) {
            country = position.getObject("country").getString("name");
            cc = position.getObject("country").getString("cc");
            alpha3 = position.getObject("country").getString("alpha3");
            timezone = position.getString("timezone");
        }

        /**
         * Gets the country.
         *
         * @return the country
         */
        public String getCountry() {
            return country;
        }

        /**
         * Gets the cc.
         *
         * @return the cc
         */
        public String getCc() {
            return cc;
        }

        /**
         * Gets the alpha3.
         *
         * @return the alpha3
         */
        public String getAlpha3() {
            return alpha3;
        }

        /**
         * Gets the timezone.
         *
         * @return the timezone
         */
        public String getTimezone() {
            return timezone;
        }
    }

    /**
     * A profile teaser.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Teaser {

        private final String type;
        private final String value;

        /**
         * Constructs a new Teaser.
         *
         * @param teaser the {@link DataObject} to construct the Teaser from
         */
        public Teaser(DataObject teaser) {
            type = teaser.getString("type");
            value = teaser.getString("value");
        }

        /**
         * Gets the type of the teaser, e.g. Job.
         *
         * @return the type of the teaser
         */
        public String getType() {
            return type;
        }

        /**
         * Gets the actual value of the teaser, e.g. Programmer.
         *
         * @return the actual value of the teaser
         */
        public String getValue() {
            return value;
        }
    }
}
