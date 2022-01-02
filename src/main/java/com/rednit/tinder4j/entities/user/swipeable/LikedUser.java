package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

/**
 * Subtype of {@link SwipeableUser} for users the self user liked.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see SwipeableUser
 * @since 1.0.0
 */
public class LikedUser extends SwipeableUser {

    private final String contentHash;
    private final boolean superLiked;
    private final long expireTime;

    /**
     * Constructs a new LikedUser.
     *
     * @param user   the {@link DataObject} to construct the LikedUser from
     * @param client the corresponding {@link TinderClient} instance
     */
    public LikedUser(DataObject user, TinderClient client) {
        super(user, client);
        contentHash = user.getString("content_hash");
        superLiked = user.getBoolean("has_been_superliked");
        expireTime = user.getLong("expire_time");
    }

    /**
     * Gets the contentHash.
     *
     * @return the contentHash
     */
    public String getContentHash() {
        return contentHash;
    }

    /**
     * Whether the user has been superliked.
     *
     * @return {@code true} if the user has been superliked
     */
    public boolean hasBeenSuperLiked() {
        return superLiked;
    }

    /**
     * Gets the time this user won't be available via {@link TinderClient#getLikedUsers()} anymore.
     *
     * @return the time this user expires
     */
    public long getExpireTime() {
        return expireTime;
    }

    @Override
    public String toString() {
        return String.format("LikedUser{id: %s / name: %s}", getId(), getName());
    }
}
