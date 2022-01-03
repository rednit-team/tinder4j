package com.rednit.tinder4j.api.entities.photo;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.Entity;
import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Top level class for photos. All photo objects at least contain the information held inside this class.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see Algorithm
 * @since 1.0.0
 */
public class GenericPhoto extends Entity {

    private final Algorithm.CropInfo cropInfo;
    private final String url;
    private final List<SizedImage> processedFiles;
    private final String fileName;
    private final String extension;
    private final String type;

    /**
     * Constructs a new GenericPhoto.
     *
     * @param photo  the {@link DataObject} to construct the GenericPhoto from
     * @param client the corresponding {@link TinderClient} instance
     */
    @SuppressWarnings("unchecked")
    public GenericPhoto(DataObject photo, TinderClient client) {
        super(photo, client);
        cropInfo = new Algorithm.CropInfo(photo.getObject("crop_info"));
        url = photo.getString("url");
        processedFiles = new ArrayList<>();
        photo.getArray("processedFiles").forEach(object ->
                processedFiles.add(new SizedImage(new DataObject((Map<String, Object>) object)))
        );
        fileName = photo.getString("fileName");
        extension = photo.getString("extension");

        if (photo.hasKey("type")) {
            type = photo.getString("type");
        } else {
            type = photo.getString("media_type");
        }
    }

    /**
     * Gets the {@link com.rednit.tinder4j.api.entities.photo.Algorithm.CropInfo CropInfo}.
     *
     * @return the {@link com.rednit.tinder4j.api.entities.photo.Algorithm.CropInfo CropInfo}
     */
    public Algorithm.CropInfo getCropInfo() {
        return cropInfo;
    }

    /**
     * Gets the url to the photo in original resolution.
     *
     * @return the url to the photo
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets a {@link List} of {@link SizedImage SizedImages}. Each {@link SizedImage} has a different resolution.
     *
     * @return a {@link List} of {@link SizedImage SizedImages}
     */
    public List<SizedImage> getProcessedFiles() {
        return processedFiles;
    }

    /**
     * Gets the filename.
     *
     * @return the filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the file extension.
     *
     * @return the file extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Gets the type of the photo. Even if it contradicts the name, this can also be a video, but Tinder still handles
     * them as photos.
     *
     * @return the of the photo.
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("GenericPhoto{id: %s / url: %s}", getId(), url);
    }
}
