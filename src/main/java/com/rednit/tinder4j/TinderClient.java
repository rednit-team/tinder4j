package com.rednit.tinder4j;

import com.rednit.tinder4j.entities.Match;
import com.rednit.tinder4j.entities.Update;
import com.rednit.tinder4j.entities.user.LikePreview;
import com.rednit.tinder4j.entities.user.SelfUser;
import com.rednit.tinder4j.entities.user.swipeable.LikedUser;
import com.rednit.tinder4j.entities.user.swipeable.Recommendation;
import com.rednit.tinder4j.entities.user.swipeable.UserProfile;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.internal.requests.DataArray;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.internal.requests.Requester;
import com.rednit.tinder4j.internal.requests.Route;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class TinderClient {

    private final Requester requester;
    private final ForkJoinPool callbackPool;

    public TinderClient(String token) {
        this.requester = new Requester(token);
        callbackPool = ForkJoinPool.commonPool();
    }

    public void awaitShutdown() {
        boolean finished = callbackPool.awaitQuiescence(60, TimeUnit.SECONDS);
        if (!finished) {
            throw new IllegalStateException("Timed out while waiting for callback threads to finish!");
        }
    }

    public RestAction<Update> getUpdates(@Nullable String lastActivityDate) {
        if (lastActivityDate == null) {
            lastActivityDate = new SimpleDateFormat("yyyy-MMMM-ddTHH:mm:ss.00Z").format(new Date());
        }
        RequestBody body = RequestBody.create(
                DataObject.empty()
                        .put("nudge", true)
                        .put("last_activity_date", lastActivityDate)
                        .toString(),
                MediaType.parse("application/json")
        );
        return new RestActionImpl<>(this, Route.Self.GET_UPDATES.compile(), body, (response, request) ->
                new Update(DataObject.fromJson(response.body()))
        );
    }

    @SuppressWarnings("unchecked")
    public RestAction<List<Recommendation>> getRecommendations() {
        return new RestActionImpl<>(this, Route.Self.GET_LIKE_PREVIEWS.compile(), (response, request) -> {
            List<Recommendation> likePreviews = new ArrayList<>();
            DataObject data = DataObject.fromJson(response.body());
            data.getArray("results").forEach(object ->
                    likePreviews.add(new Recommendation(new DataObject((Map<String, Object>) object), this))
            );
            return likePreviews;
        });
    }

    @SuppressWarnings("unchecked")
    public RestAction<List<LikePreview>> getLikePreviews() {
        return new RestActionImpl<>(this, Route.Self.GET_LIKE_PREVIEWS.compile(), (response, request) -> {
            List<LikePreview> likePreviews = new ArrayList<>();
            DataObject data = DataObject.fromJson(response.body());
            data.getObject("data").getArray("results").forEach(object -> {
                DataObject preview = new DataObject((Map<String, Object>) object);
                likePreviews.add(new LikePreview(preview.getObject("user"), this));
            });
            return likePreviews;
        });
    }

    public void loadAllMatches() {

    }

    public RestAction<Match> getMatch(String id) {
        return new RestActionImpl<>(this, Route.Match.GET_MATCH.compile(id), (response, request) ->
                new Match(DataObject.fromJson(response.body()), this)
        );
    }

    public RestAction<UserProfile> getUserProfile(String id) {
        return new RestActionImpl<>(this, Route.User.GET_USER.compile(id), (response, request) ->
                new UserProfile(DataObject.fromJson(response.body()), this)
        );
    }

    public RestAction<SelfUser> getSelfUser() {
        return new RestActionImpl<>(this, Route.Self.GET_SELF.compile(), (response, request) ->
                new SelfUser(DataObject.fromJson(response.body()), this)
        );
    }

    @SuppressWarnings("unchecked")
    public RestAction<List<LikedUser>> getLikedUsers() {
        return new RestActionImpl<>(this, Route.Self.GET_LIKED_USERS.compile(), (response, request) -> {
            List<LikedUser> likedUsers = new ArrayList<>();
            DataObject data = DataObject.fromJson(response.body());
            DataArray result = DataArray.empty();
            data.getObject("data").getArray("results").forEach(object -> {
                DataObject user = new DataObject((Map<String, Object>) object);
                DataObject transformed = new DataObject(user.toMap());
                transformed.remove("type");
                transformed.remove("user");
                user.getObject("user").toMap().forEach(transformed::put);
                result.add(transformed);
            });
            result.forEach(object ->
                    likedUsers.add(new LikedUser(new DataObject((Map<String, Object>) object), this))
            );
            return likedUsers;
        });
    }

    public Requester getRequester() {
        return requester;
    }

    public ExecutorService getCallbackPool() {
        return callbackPool;
    }
}
