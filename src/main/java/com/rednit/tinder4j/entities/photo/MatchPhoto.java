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
    private final double score;
    private final int winCount;

    @SuppressWarnings("unchecked")
    public MatchPhoto(DataObject photo, TinderClient client) {
        super(photo, client);
        assets = new ArrayList<>();
        photo.getArray("assets").forEach(object ->
                assets.add(new SizedImage(new DataObject((Map<String, Object>) object)))
        );

        if (getType().equals("image")) {
            if (!photo.hasKey("webp_qf")) {
               webpQf = 0;
            } else {
                webpQf = (int) photo.get("webp_qf", List.class).get(0);
            }
            rank = photo.getInteger("rank");
            score = photo.getDouble("score");
            winCount = photo.getInteger("win_count");
        } else {
            webpQf = 0;
            rank = 0;
            score = 0;
            winCount = 0;
        }
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

    public double getScore() {
        return score;
    }

    public int getWinCount() {
        return winCount;
    }
}
