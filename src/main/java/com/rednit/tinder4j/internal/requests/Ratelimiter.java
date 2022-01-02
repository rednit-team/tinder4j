package com.rednit.tinder4j.internal.requests;

/**
 * A Ratelimiter decides whether a {@link Request} should be delayed and if yes for how long.
 * <p>
 * The Tinder API has no official rate limiting, but API spamming results in extra verification needed, shadow-bans
 * or complete account suspension.
 * </p><p>
 * Thus, the default Ratelimiter of the library is pretty restrictive. Use
 * {@link com.rednit.tinder4j.TinderClient#setRatelimiter(Ratelimiter) setRatelimiter(Ratelimiter)} to use your own
 * Ratelimiter implementation.
 * </p>
 *
 * @author Kaktushose
 * @version 1.0.0
 * @see com.rednit.tinder4j.TinderClient#setRatelimiter(Ratelimiter) setRatelimiter(Ratelimiter)
 * @since 1.0.0
 */
public interface Ratelimiter {

    /**
     * Whether a request should be delayed in flavor or rate limits.
     *
     * @param request the corresponding {@link Request}
     * @return {@code true} if the request should be delayed
     */
    boolean shouldDelay(Request<?> request);

    /**
     * Gets the delay a request should be delayed with in ms.
     *
     * @param request the corresponding {@link Request}
     * @return the delay in ms
     */
    long getDelay(Request<?> request);
}
