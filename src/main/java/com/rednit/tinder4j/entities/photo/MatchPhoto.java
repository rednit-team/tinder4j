package com.rednit.tinder4j.entities.photo;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchPhoto extends GenericPhoto {

    private final List<SizedImage> assets;
    private final int webpQf;
    private final int rank;
    private final float score;
    private final int winCount;

    @SuppressWarnings("unchecked")
    public MatchPhoto(DataObject photo, TinderClient client) {
        super(photo, client);
        assets = new ArrayList<>();
        photo.getArray("assets").forEach(object ->
                assets.add(new SizedImage(new DataObject((Map<String, Object>) object)))
        );
        webpQf = photo.getInteger("webp_qf");
        rank = photo.getInteger("rank");
        score = photo.getFloat("score");
        winCount = photo.getInteger("win_count");
    }

    public boolean hasAssets() {
        return !assets.isEmpty();
    }

    public List<SizedImage> getAssets() {
        return assets;
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
}
