package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Algorithm {

    public static class FacialScope {

        private final float widthPct;
        private final float xOffsetPct;
        private final float heightPct;
        private final float yOffsetPct;

        public FacialScope(DataObject scope) {
            widthPct = scope.getFloat("width_pct");
            xOffsetPct = scope.getFloat("x_offset_pct");
            heightPct = scope.getFloat("height_pct");
            yOffsetPct = scope.getFloat("y_offset_pct");
        }

        public float getWidthPct() {
            return widthPct;
        }

        public float getXOffsetPct() {
            return xOffsetPct;
        }

        public float getHeightPct() {
            return heightPct;
        }

        public float getYOffsetPct() {
            return yOffsetPct;
        }
    }

    public static class Face {

        private final FacialScope algorithm;
        private final float boundingBoxPercentage;

        public Face(DataObject face) {
            algorithm = new FacialScope(face.getObject("algo"));
            boundingBoxPercentage = face.getFloat("bounding_box_percentage");
        }

        public FacialScope getAlgorithm() {
            return algorithm;
        }

        public float getBoundingBoxPercentage() {
            return boundingBoxPercentage;
        }
    }

    public static class CropInfo {

        private final float boundingBoxPercentage;
        private final boolean userCustomized;
        private final List<FacialScope> faces;
        private final FacialScope user;
        private final FacialScope algorithm;

        @SuppressWarnings("unchecked")
        public CropInfo(DataObject cropInfo) {
            boundingBoxPercentage = cropInfo.getFloat("bounding_box_percentage");
            userCustomized = cropInfo.getBoolean("user_customized");

            if (cropInfo.hasKey("user")) {
                user = new FacialScope(cropInfo.getObject("user"));
            } else {
                user = null;
            }

            if (cropInfo.hasKey("algo")) {
                algorithm = new FacialScope(cropInfo.getObject("algo"));
            } else {
                algorithm = null;
            }

            faces = new ArrayList<>();
            if (cropInfo.hasKey("faces")) {
                cropInfo.getArray("faces").forEach(object ->
                        faces.add(new FacialScope(new DataObject((Map<String, Object>) object)))
                );
            }
        }

        public float getBoundingBoxPercentage() {
            return boundingBoxPercentage;
        }

        public boolean isUserCustomized() {
            return userCustomized;
        }

        public boolean hasFaces() {
            return !faces.isEmpty();
        }

        public List<FacialScope> getFaces() {
            return faces;
        }

        public Optional<FacialScope> getUser() {
            return Optional.ofNullable(user);
        }

        public Optional<FacialScope> getAlgorithm() {
            return Optional.ofNullable(algorithm);
        }
    }

    public static class Hash {

        private final String version;
        private final String value;

        public Hash(DataObject hash) {
            version = hash.getString("version");
            value = hash.getString("value");
        }

        public String getVersion() {
            return version;
        }

        public String getValue() {
            return value;
        }
    }
}
