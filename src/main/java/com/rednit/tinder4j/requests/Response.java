package com.rednit.tinder4j.requests;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

public class Response implements Closeable {

    private final int code;
    private final okhttp3.Response rawResponse;

    public Response(okhttp3.Response rawResponse) {
        this.code = rawResponse.code();
        this.rawResponse = rawResponse;
    }

    public String body() {
        try {
            return Objects.requireNonNull(rawResponse.body()).string();
        } catch (IOException | NullPointerException e) {
            return "{}";
        }
    }

    public int getCode() {
        return code;
    }

    public boolean isError() {
        return !isOk() && !isRateLimit();
    }

    public boolean isOk() {
        return this.code > 199 && this.code < 300;
    }

    public boolean isRateLimit() {
        return this.code == 429;
    }

    @Override
    public void close() {
        rawResponse.close();
    }
}
