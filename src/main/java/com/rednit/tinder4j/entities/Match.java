package com.rednit.tinder4j.entities;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.message.Message;
import com.rednit.tinder4j.entities.photo.MatchPhoto;
import com.rednit.tinder4j.entities.user.MatchedUser;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.internal.requests.Route;
import com.rednit.tinder4j.utils.MessageCacheView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.Optional;

public class Match extends Entity {

    private final boolean closed;
    private final String createdDate;
    private final boolean dead;
    private final String lastActivityDate;
    private final boolean pending;
    private final boolean isSuperLike;
    private final boolean isBoostMatch;
    private final boolean isSuperBoostMatch;
    private final boolean isExperiencesMatch;
    private final boolean isFastMatch;
    private final boolean isOpener;
    private final boolean following;
    private final boolean followingMoments;
    private final MatchedUser matchedUser;
    private final MatchPhoto likedContent;
    private final boolean seen;
    private final String lastSeenMessageId;
    private final MessageCacheView messageCacheView;

    public Match(DataObject match, TinderClient client) {
        super(match, client);
        closed = match.getBoolean("closed");
        createdDate = match.getString("created_date");
        dead = match.getBoolean("dead");
        lastActivityDate = match.getString("last_activity_date");
        pending = match.getBoolean("pending");
        isSuperLike = match.getBoolean("is_super_like");
        isBoostMatch = match.getBoolean("is_boost_match");
        isSuperBoostMatch = match.getBoolean("is_super_boost_match");
        isExperiencesMatch = match.getBoolean("is_experiences_match");
        isFastMatch = match.getBoolean("is_fast_match");
        isOpener = match.getBoolean("is_opener");
        following = match.getBoolean("following");
        followingMoments = match.getBoolean("following_moments");
        matchedUser = new MatchedUser(match.getObject("person"), getClient());
        if (match.hasKey("liked_content")) {
            if (isOpener) {
                likedContent = new MatchPhoto(
                        match.getObject("liked_content").getObject("by_closer").getObject("photo"), getClient()
                );
            } else {
                likedContent = new MatchPhoto(
                        match.getObject("liked_content").getObject("by_opener").getObject("photo"), getClient()
                );
            }
        } else {
            likedContent = null;
        }
        if (match.hasKey("seen")) {
            DataObject seenObject = match.getObject("seen");
            seen = seenObject.getBoolean("match_seen");
            if (seenObject.hasKey("last_seen_message_id")) {
                lastSeenMessageId = seenObject.getString("last_seen_message_id");
            } else {
                lastSeenMessageId = null;
            }
        } else {
            seen = false;
            lastSeenMessageId = null;
        }
        messageCacheView = new MessageCacheView(getId(), getClient());
    }

    public boolean isClosed() {
        return closed;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public boolean isDead() {
        return dead;
    }

    public String getLastActivityDate() {
        return lastActivityDate;
    }

    public boolean isPending() {
        return pending;
    }

    public boolean isSuperLike() {
        return isSuperLike;
    }

    public boolean isBoostMatch() {
        return isBoostMatch;
    }

    public boolean isSuperBoostMatch() {
        return isSuperBoostMatch;
    }

    public boolean isExperiencesMatch() {
        return isExperiencesMatch;
    }

    public boolean isFastMatch() {
        return isFastMatch;
    }

    public boolean isOpener() {
        return isOpener;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isFollowingMoments() {
        return followingMoments;
    }

    public MatchedUser getMatchedUser() {
        return matchedUser;
    }

    public Optional<MatchPhoto> getLikedContent() {
        return Optional.ofNullable(likedContent);
    }

    public boolean isSeen() {
        return seen;
    }

    public Optional<String> getLastSeenMessageId() {
        return Optional.ofNullable(lastSeenMessageId);
    }

    public MessageCacheView getMessageCacheView() {
        return messageCacheView;
    }

    public RestAction<Message> sendMessage(String content) {
        RequestBody body = RequestBody.create(
                DataObject.empty().put("message", content).toString(),
                MediaType.parse("application/json")
        );
        return new RestActionImpl<>(
                getClient(),
                Route.Match.SEND_MESSAGE.compile(getId()),
                body,
                (response, request) -> new Message(DataObject.fromJson(response.body()), getClient())
        );
    }

    public RestAction<Void> delete() {
        getClient().getMatchCacheView().remove(getId());
        return new RestActionImpl<>(getClient(), Route.Match.DELETE_MATCH.compile(getId()));
    }

    @Override
    public String toString() {
        return String.format("Match{id: %s / %s}", getId(), matchedUser);
    }
}
