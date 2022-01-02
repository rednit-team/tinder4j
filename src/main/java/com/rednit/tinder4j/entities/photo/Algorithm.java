package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class contains all wrapped algorithm models.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class Algorithm {

    /**
     * A Facial Scope contains coordinates to locate faces inside a photo.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class FacialScope {

        private final double widthPct;
        private final double xOffsetPct;
        private final double heightPct;
        private final double yOffsetPct;

        public FacialScope(DataObject scope) {
            widthPct = scope.getDouble("width_pct");
            xOffsetPct = scope.getDouble("x_offset_pct");
            heightPct = scope.getDouble("height_pct");
            yOffsetPct = scope.getDouble("y_offset_pct");
        }

        public double getWidthPct() {
            return widthPct;
        }

        public double getXOffsetPct() {
            return xOffsetPct;
        }

        public double getHeightPct() {
            return heightPct;
        }

        public double getYOffsetPct() {
            return yOffsetPct;
        }
    }

    /**
     * A Face inside a photo.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Face {

        private final FacialScope algorithm;
        private final double boundingBoxPercentage;

        public Face(DataObject face) {
            algorithm = new FacialScope(face.getObject("algo"));
            boundingBoxPercentage = face.getDouble("bounding_box_percentage");
        }

        public FacialScope getAlgorithm() {
            return algorithm;
        }

        public double getBoundingBoxPercentage() {
            return boundingBoxPercentage;
        }
    }

    /**
     * Photo metadata and information about faces and used algorithms.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class CropInfo {

        private final boolean userCustomized;
        private final List<Face> faces;
        private final FacialScope user;
        private final FacialScope algorithm;

        @SuppressWarnings("unchecked")
        public CropInfo(DataObject cropInfo) {
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
                        faces.add(new Face(new DataObject((Map<String, Object>) object)))
                );
            }
        }

        public boolean isUserCustomized() {
            return userCustomized;
        }

        public boolean hasFaces() {
            return !faces.isEmpty();
        }

        public List<Face> getFaces() {
            return faces;
        }

        public Optional<FacialScope> getUser() {
            return Optional.ofNullable(user);
        }

        public Optional<FacialScope> getAlgorithm() {
            return Optional.ofNullable(algorithm);
        }
    }

    /**
     * A photo hash.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
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
