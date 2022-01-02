package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

/**
 * Subtype of {@link SwipeableUser} for users the self user gets recommended.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see SwipeableUser
 * @since 1.0.0
 */
public class Recommendation extends SwipeableUser {

    private final boolean groupMatched;
    private final String contentHash;

    /**
     * Constructs a new Recommendation.
     *
     * @param user   the {@link DataObject} to construct the Recommendation from
     * @param client the corresponding {@link TinderClient} instance
     */
    public Recommendation(DataObject user, TinderClient client) {
        super(user, client);
        groupMatched = user.getBoolean("group_matched");
        contentHash = user.getString("content_hash");
    }

    /**
     * Whether the user is group matched. <em>Developer note: I have no idea what group matched means.</em>
     *
     * @return {@code true} if the user is group matched
     */
    public boolean isGroupMatched() {
        return groupMatched;
    }

    /**
     * Gets the contentHash.
     *
     * @return the contentHash
     */
    public String getContentHash() {
        return contentHash;
    }

    @Override
    public String toString() {
        return String.format("Recommendation{id: %s / name: %s}", getId(), getName());
    }
}
