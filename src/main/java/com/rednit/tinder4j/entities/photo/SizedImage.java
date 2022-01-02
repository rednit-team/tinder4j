package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.Optional;

/**
 * An image with a fixed size. Used in various places inside the API.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class SizedImage {

    private final int width;
    private final int height;
    private final String url;
    private final String quality;

    /**
     * Constructs a new SizedImage.
     *
     * @param image the {@link DataObject} to construct the SizedImage from
     */
    public SizedImage(DataObject image) {
        width = image.getInteger("width");
        height = image.getInteger("height");
        url = image.getString("url");

        if (image.hasKey("quality")) {
            quality = image.getString("quality");
        } else {
            quality = null;
        }
    }

    /**
     * Gets the width of the image.
     *
     * @return the width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the image.
     *
     * @return the height of the image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the url to the image.
     *
     * @return the url to the photo
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the quality of the image. This is only used inside
     * {@link com.rednit.tinder4j.entities.user.Metadata.Descriptor Descriptors}.
     *
     * @return an {@link Optional} holding the quality of the image
     */
    public Optional<String> getQuality() {
        return Optional.ofNullable(quality);
    }

    @Override
    public String toString() {
        return String.format("SizedImage{url: %s}", url);
    }
}
