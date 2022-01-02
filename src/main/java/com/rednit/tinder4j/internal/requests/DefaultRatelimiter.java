package com.rednit.tinder4j.internal.requests;

import java.util.concurrent.ThreadLocalRandom;

public class DefaultRatelimiter implements Ratelimiter {

    private int exceedCount;
    private long lastRequest;

    @Override
    public boolean shouldDelay(Request<?> request) {
        long current = System.currentTimeMillis();
        if (current - lastRequest > 5000) {
            lastRequest = current;
            return false;
        }
        lastRequest = current;
        exceedCount++;
        if (exceedCount > 1) {
            exceedCount = 0;
            return true;
        }
        return false;
    }

    @Override
    public long getDelay(Request<?> request) {
        return ((long) (ThreadLocalRandom.current().nextFloat() * 5 + 1)) * 1000;
    }
}
