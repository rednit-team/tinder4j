package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public boolean hasAssets() {
        return !assets.isEmpty();
    }

    public List<SizedImage> getAssets() {
        return assets;
    }

    public String getCreatedAt() {
        return createdAt;
    }

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
