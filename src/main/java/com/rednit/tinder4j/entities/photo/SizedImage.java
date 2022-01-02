package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.Optional;

public class SizedImage {

    private final int width;
    private final int height;
    private final String url;
    private final String quality;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public Optional<String> getQuality() {
        return Optional.ofNullable(quality);
    }

    @Override
    public String toString() {
        return String.format("SizedImage{url: %s}", url);
    }
}
