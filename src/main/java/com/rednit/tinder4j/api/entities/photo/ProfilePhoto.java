package com.rednit.tinder4j.api.entities.photo;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Subtype of {@link GenericPhoto}. This type of photo can be only present inside the
 * {@link com.rednit.tinder4j.api.entities.user.SelfUser SelfUser}
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see GenericPhoto
 * @since 1.0.0
 */
public class ProfilePhoto extends GenericPhoto {

    private final List<SizedImage> assets;
    private final String createdAt;
    private final String updatedAt;
    private final String fbId;
    private final int webpQf;
    private final int rank;
    private final float score;
    private final int winCount;
    private final Algorithm.Hash pHash;
    private final Algorithm.Hash dHash;

    /**
     * Constructs a new ProfilePhoto.
     *
     * @param photo  the {@link DataObject} to construct the ProfilePhoto from
     * @param client the corresponding {@link TinderClient} instance
     */
    @SuppressWarnings("unchecked")
    public ProfilePhoto(DataObject photo, TinderClient client) {
        super(photo, client);
        assets = new ArrayList<>();
        photo.getArray("assets").forEach(object ->
                assets.add(new SizedImage(new DataObject((Map<String, Object>) object)))
        );
        createdAt = photo.getString("created_at");
        updatedAt = photo.getString("updated_at");
        fbId = photo.getString("fbId");
        webpQf = photo.getInteger("webp_qf");
        rank = photo.getInteger("rank");
        score = photo.getFloat("score");
        winCount = photo.getInteger("win_count");
        pHash = new Algorithm.Hash(photo.getObject("phash"));
        dHash = new Algorithm.Hash(photo.getObject("dhash"));
    }

    /**
     * Whether this MatchPhoto has assets.
     *
     * @return {@code true} if this MatchPhoto has assets
     * @see MatchPhoto#getAssets()
     */
    public boolean hasAssets() {
        return !assets.isEmpty();
    }

    /**
     * Gets a possibly empty {@link List} of {@link SizedImage SizedImages}. Each {@link SizedImage} has the same
     * resolution, but a different zoom level on the phase.
     *
     * @return a possibly empty {@link List} of {@link SizedImage SizedImages}
     */
    public List<SizedImage> getAssets() {
        return assets;
    }

    /**
     * Gets the date the photo was created.
     *
     * @return the date the photo was created
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the date the photo was updated.
     *
     * @return the date the photo was updated
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getFbId() {
        return fbId;
    }

    public int getWebpQf() {
        return webpQf;
    }

    public int getRank() {
        return rank;
    }

    public float getScore() {
        return score;
    }

    public int getWinCount() {
        return winCount;
    }

    public Algorithm.Hash getPHash() {
        return pHash;
    }

    public Algorithm.Hash getDHash() {
        return dHash;
    }
}
