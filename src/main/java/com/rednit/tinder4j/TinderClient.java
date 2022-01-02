package com.rednit.tinder4j;

import com.rednit.tinder4j.entities.Update;
import com.rednit.tinder4j.entities.user.LikePreview;
import com.rednit.tinder4j.entities.user.SelfUser;
import com.rednit.tinder4j.entities.user.swipeable.LikedUser;
import com.rednit.tinder4j.entities.user.swipeable.Recommendation;
import com.rednit.tinder4j.entities.user.swipeable.UserProfile;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.exceptions.LoginException;
import com.rednit.tinder4j.internal.requests.*;
import com.rednit.tinder4j.utils.MatchCacheView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class TinderClient {

    private final Logger log = LoggerFactory.getLogger(TinderClient.class);
    private final Requester requester;
    private final ForkJoinPool callbackPool;
    private final MatchCacheView matchCache;
    private Ratelimiter ratelimiter;

    public TinderClient(String token) {
        this.requester = new Requester(token);
        callbackPool = ForkJoinPool.commonPool();
        matchCache = new MatchCacheView(this);
        ratelimiter = new DefaultRatelimiter();
        try {
            getSelfUser().complete();
        } catch (CompletionException ignored) {
            throw new LoginException("The provided token is invalid!");
        }
        log.info("Login successful!");
    }

    public void awaitShutdown() {
        boolean finished = callbackPool.awaitQuiescence(60, TimeUnit.SECONDS);
        if (!finished) {
            throw new IllegalStateException("Timed out while waiting for callback threads to finish!");
        }
        log.info("Shutdown complete!");
    }

    public RestAction<Update> getUpdates(@Nullable String lastActivityDate) {
        if (lastActivityDate == null || "".equals(lastActivityDate)) {
            lastActivityDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.00'Z'").format(new Date());
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
        return new RestActionImpl<>(this, Route.Self.GET_RECOMMENDATIONS.compile(), (response, request) -> {
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

    public MatchCacheView getMatchCacheView() {
        return matchCache;
    }

    public RestAction<UserProfile> getUserProfile(String id) {
        return new RestActionImpl<>(this, Route.User.GET_USER.compile(id), (response, request) ->
                new UserProfile(DataObject.fromJson(response.body()).getObject("results"), this)
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
                DataObject transformed = new DataObject(new HashMap<>(user.toMap()));
                transformed.remove("type");
                transformed.remove("user");
                user.getObject("user").toMap().forEach(transformed::put);
                result.add(transformed);
            });
            result.forEach(object ->
                    likedUsers.add(new LikedUser((DataObject) object, this))
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

    public Ratelimiter getRatelimiter() {
        return ratelimiter;
    }

    public void setRatelimiter(Ratelimiter ratelimiter) {
        this.ratelimiter = ratelimiter;
    }
}
