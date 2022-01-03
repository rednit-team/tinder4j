package com.rednit.tinder4j.api.entities;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.message.Message;
import com.rednit.tinder4j.api.entities.photo.MatchPhoto;
import com.rednit.tinder4j.api.entities.user.MatchedUser;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.async.RestActionImpl;
import com.rednit.tinder4j.requests.DataObject;
import com.rednit.tinder4j.requests.Route;
import com.rednit.tinder4j.api.cache.MessageCacheView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.Optional;

/**
 * Represents a Tinder match.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
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

    /**
     * Constructs a new Match.
     *
     * @param match  the {@link DataObject} to construct the Match from
     * @param client the corresponding {@link TinderClient} instance
     */
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

    /**
     * Whether the match is closed.
     *
     * @return {@code true} if the match is closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Gets the date the match was created.
     *
     * @return the date the match was created
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Whether the match is dead.
     *
     * @return {@code true} if the match is dead
     */
    public boolean isDead() {
        return dead;
    }

    public String getLastActivityDate() {
        return lastActivityDate;
    }

    /**
     * Whether the match is pending.
     *
     * @return {@code true} if the match is pending
     */
    public boolean isPending() {
        return pending;
    }

    /**
     * Whether one of the participants used a super like.
     *
     * @return {@code true} if one of the participants used a super like
     */
    public boolean isSuperLike() {
        return isSuperLike;
    }

    /**
     * Whether one of the participants used a boost.
     *
     * @return {@code true} if one of the participants used a boost
     */
    public boolean isBoostMatch() {
        return isBoostMatch;
    }

    /**
     * Whether one of the participants used a boost and a super like.
     *
     * @return {@code true} if one of the participants used a boost and a super like
     */
    public boolean isSuperBoostMatch() {
        return isSuperBoostMatch;
    }

    /**
     * Whether the match is an experiences match.
     *
     * @return {@code true} if the match is an experiences match
     */
    public boolean isExperiencesMatch() {
        return isExperiencesMatch;
    }

    /**
     * Whether the match is a fast match.
     *
     * @return {@code true} if the match is a fast match
     */
    public boolean isFastMatch() {
        return isFastMatch;
    }

    /**
     * Whether the self user or the matched user liked first.
     *
     * @return {@code true} if the self user liked first
     */
    public boolean isOpener() {
        return isOpener;
    }

    /**
     * Whether one of the participants is following. <em>Following what?</em> you ask? Same, bro, same...
     *
     * @return {@code true} if one of the participants is following
     */
    public boolean isFollowing() {
        return following;
    }

    /**
     * Whether one of the participants is following moments.
     *
     * @return {@code true} if one of the participants is following moments
     */
    public boolean isFollowingMoments() {
        return followingMoments;
    }

    /**
     * Gets the {@link MatchedUser}.
     *
     * @return the {@link MatchedUser}
     */
    public MatchedUser getMatchedUser() {
        return matchedUser;
    }

    /**
     * Gets the {@link MatchPhoto} one of the participants liked.
     *
     * @return an {@link Optional} holding the {@link MatchPhoto} one of the participants liked
     */
    public Optional<MatchPhoto> getLikedContent() {
        return Optional.ofNullable(likedContent);
    }

    /**
     * Whether the match has been seen.
     *
     * @return {@code true} if the match has been seen
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Gets the last seen message id.
     *
     * @return an {@link Optional} holding the last seen message id
     */
    public Optional<String> getLastSeenMessageId() {
        return Optional.ofNullable(lastSeenMessageId);
    }

    /**
     * Gets the {@link MessageCacheView}.
     *
     * @return the {@link MessageCacheView}
     */
    public MessageCacheView getMessageCacheView() {
        return messageCacheView;
    }

    /**
     * Sends a message.
     *
     * @param content the message to send
     * @return a {@link RestAction} holding the {@link Message} sent
     */
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

    /**
     * Deletes the match.
     *
     * @return {@link RestAction}
     */
    public RestAction<Void> delete() {
        getClient().getMatchCacheView().remove(getId());
        return new RestActionImpl<>(getClient(), Route.Match.DELETE_MATCH.compile(getId()));
    }

    @Override
    public String toString() {
        return String.format("Match{id: %s / %s}", getId(), matchedUser);
    }
}
