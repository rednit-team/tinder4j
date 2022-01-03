package com.rednit.tinder4j.api.entities.photo;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Subtype of {@link GenericPhoto}. This type of photo can be only present inside a
 * {@link com.rednit.tinder4j.api.entities.Match Match} or a
 * {@link com.rednit.tinder4j.api.entities.user.MatchedUser MatchedUser}.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see GenericPhoto
 * @since 1.0.0
 */
public class MatchPhoto extends GenericPhoto {

    private final List<SizedImage> assets;
    private final int webpQf;
    private final int rank;
    private final double score;
    private final int winCount;

    /**
     * Constructs a new MatchPhoto.
     *
     * @param photo  the {@link DataObject} to construct the MatchPhoto from
     * @param client the corresponding {@link TinderClient} instance
     */
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
