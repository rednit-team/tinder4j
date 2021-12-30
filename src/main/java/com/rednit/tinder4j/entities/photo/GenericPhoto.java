package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.Entity;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericPhoto extends Entity {

    private final Algorithm.CropInfo cropInfo;
    private final String url;
    private final List<SizedImage> processedFiles;
    private final String fileName;
    private final String extension;
    private final String type;

    public GenericPhoto(DataObject photo, TinderClient client) {
        super(photo, client);
        cropInfo = new Algorithm.CropInfo(photo.getObject("crop_info"));
        url = photo.getString("url");
        processedFiles = new ArrayList<>();
        photo.getArray("processedFiles").forEach(object -> {});
        fileName = photo.getString("fileName");
        extension = photo.getString("extension");

        if (photo.hasKey("type")) {
            type = photo.getString("type");
        } else {
            type = photo.getString("media_type");
        }
    }

    public Algorithm.CropInfo getCropInfo() {
        return cropInfo;
    }

    public String getUrl() {
        return url;
    }

    public List<SizedImage> getProcessedFiles() {
        return processedFiles;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }
}
