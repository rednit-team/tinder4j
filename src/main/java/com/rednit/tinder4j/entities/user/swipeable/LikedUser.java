package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

public class LikedUser extends SwipeableUser {

    private final String contentHash;
    private final boolean superLiked;
    private final long expireTime;

    public LikedUser(DataObject user, TinderClient client) {
        super(user, client);
        contentHash = user.getString("content_hash");
        superLiked = user.getBoolean("has_been_superliked");
        expireTime = user.getLong("expire_time");
    }

    public String getContentHash() {
        return contentHash;
    }

    public boolean hasBeenSuperLiked() {
        return superLiked;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
