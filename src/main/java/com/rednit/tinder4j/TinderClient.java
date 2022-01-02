package com.rednit.tinder4j;

import com.rednit.tinder4j.entities.Update;
import com.rednit.tinder4j.entities.user.LikePreview;
import com.rednit.tinder4j.entities.user.SelfUser;
import com.rednit.tinder4j.entities.user.swipeable.LikedUser;
import com.rednit.tinder4j.entities.user.swipeable.Recommendation;
import com.rednit.tinder4j.entities.user.swipeable.UserProfile;
import com.rednit.tinder4j.exceptions.LoginException;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
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

/**
 * The entry point to interact with the Tinder API. This class supports the most common endpoints that are needed to
 * interact with Tinder.
 *
 * <h1>Authentication</h1>
 *
 * Tinder uses Basic Authentication with UUID strings. To get your token, first login to Tinder in your browser.
 * Then, open the network tab and filter for api.gotinder.com. Choose any GET or POST request and go to the Request
 * Headers. There, you'll find the X-Auth-Token header containing the auth token. Please note: you might need to perform
 * some actions first (for example liking a user) before you see any requests.
 *
 * <h1>Rate Limiting</h1>
 *
 * The Tinder API has no official rate limiting, but API spamming results in extra verification needed, shadow-bans
 * or complete account suspension. Thus, the default Ratelimiter of the library is pretty restrictive.
 * Use {@link #setRatelimiter(Ratelimiter) } to use your own Ratelimiter implementation.
 *
 * <h1>Runtime</h1>
 *
 * The client will terminate as soon as all pending API requests where sent. If you want to use this library for bots
 * or similar you have to keep the JVM or respectively the client alive by yourself. <b>This also results in callback
 * threads being killed before their execution is finished. Call {@link #awaitShutdown()} to prevent this behaviour.</b>
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see Ratelimiter
 * @since 1.0.0
 */
public class TinderClient {

    private final Logger log = LoggerFactory.getLogger(TinderClient.class);
    private final Requester requester;
    private final ForkJoinPool callbackPool;
    private final MatchCacheView matchCache;
    private Ratelimiter ratelimiter;

    /**
     * Constructs a new TinderClient.
     *
     * @param token the X-Auth-Token to authenticate
     * @throws LoginException if the provided token is invalid
     */
    public TinderClient(String token) {
        this.requester = new Requester(token);
        callbackPool = ForkJoinPool.commonPool();
        matchCache = new MatchCacheView(this);
        ratelimiter = new DefaultRatelimiter();
        if (token == null || "".equals(token)) {
            throw new LoginException("Token cannot be null or empty!");
        }
        try {
            getSelfUser().complete();
        } catch (CompletionException ignored) {
            throw new LoginException("The provided token is invalid!");
        }
        log.info("Login successful!");
    }

    /**
     * Blocks the current thread until all callback threads have finished their tasks. This will wait a maximum of 60
     * seconds.
     *
     * @throws IllegalStateException after the maximum time to wait exceeded
     */
    public void awaitShutdown() {
        boolean finished = callbackPool.awaitQuiescence(60, TimeUnit.SECONDS);
        if (!finished) {
            throw new IllegalStateException("Timed out while waiting for callback threads to finish!");
        }
        log.info("Shutdown complete!");
    }

    /**
     * Gets an {@link Update} from the Tinder API.
     *
     * @param lastActivityDate the last date of activity, can be null
     * @return a {@link RestAction} holding an {@link Update}
     */
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

    /**
     * Gets {@link Recommendation Recommendations} from the Tinder API.
     *
     * @return a {@link RestAction} holding a {@link List} of {@link Recommendation Recommendations}
     */
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

    /**
     * Gets {@link LikePreview LikePreviews} from the Tinder API.
     *
     * @return a {@link RestAction} holding a {@link List} of {@link LikePreview LikePreviews}
     */
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

    /**
     * Gets the {@link MatchCacheView}.
     *
     * @return the {@link MatchCacheView}
     */
    public MatchCacheView getMatchCacheView() {
        return matchCache;
    }

    /**
     * Gets an {@link UserProfile} from the Tinder API by id.
     *
     * @param id the id to get the user from
     * @return a {@link RestAction} holding an {@link UserProfile}
     */
    public RestAction<UserProfile> getUserProfile(String id) {
        return new RestActionImpl<>(this, Route.User.GET_USER.compile(id), (response, request) ->
                new UserProfile(DataObject.fromJson(response.body()).getObject("results"), this)
        );
    }

    /**
     * Gets the {@link SelfUser} from the Tinder API.
     *
     * @return a {@link RestAction} holding the {@link SelfUser}
     */
    public RestAction<SelfUser> getSelfUser() {
        return new RestActionImpl<>(this, Route.Self.GET_SELF.compile(), (response, request) ->
                new SelfUser(DataObject.fromJson(response.body()), this)
        );
    }

    /**
     * Gets a {@link List} of {@link LikedUser LikedUsers} from the Tinder API.
     *
     * @return a {@link RestAction} holding a {@link List} of {@link LikedUser LikedUsers}
     */
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

    /**
     * Gets the {@link Requester} used to send API requests.
     *
     * @return the {@link Requester}
     */
    public Requester getRequester() {
        return requester;
    }

    /**
     * Gets the {@link ExecutorService} used for callbacks.
     *
     * @return the {@link ExecutorService} used for callbacks
     */
    public ExecutorService getCallbackPool() {
        return callbackPool;
    }

    /**
     * Gets the {@link Ratelimiter} used to rate limit requests.
     *
     * @return the {@link Ratelimiter}
     */
    public Ratelimiter getRatelimiter() {
        return ratelimiter;
    }

    /**
     * Sets the {@link Ratelimiter} used to rate limit requests.
     *
     * @param ratelimiter the {@link Ratelimiter} to use
     * @see Ratelimiter
     */
    public void setRatelimiter(Ratelimiter ratelimiter) {
        this.ratelimiter = ratelimiter;
    }
}
