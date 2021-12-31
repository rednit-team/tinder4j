package com.rednit.tinder4j.entities.user.swipeable;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;

public class Recommendation extends SwipeableUser {

    private final boolean groupMatched;
    private final String contentHash;

    public Recommendation(DataObject user, TinderClient client) {
        super(user, client);
        groupMatched = user.getBoolean("group_matched");
        contentHash = user.getString("content_hash");
    }

    public boolean isGroupMatched() {
        return groupMatched;
    }

    public String getContentHash() {
        return contentHash;
    }
}
