package com.rednit.tinder4j.internal.requests;

public interface Ratelimiter {

    boolean shouldDelay(Request<?> request);

    long getDelay(Request<?> request);

}
