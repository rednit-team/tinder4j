package com.rednit.tinder4j.internal.async;

import com.rednit.tinder4j.internal.requests.Request;
import com.rednit.tinder4j.internal.requests.Route;
import okhttp3.RequestBody;

import java.util.concurrent.CompletableFuture;

public class RestFuture<T> extends CompletableFuture<T> {
    final Request<T> request;

    public RestFuture(RestActionImpl<T> restAction, Route.CompiledRoute route, final RequestBody data) {
        this.request = new Request<>(restAction, this::complete, this::completeExceptionally, route, data);
        restAction.getTinderClient().getRequester().request(this.request);
    }

    public RestFuture(final T t) {
        complete(t);
        this.request = null;
    }

    public RestFuture(final Throwable t) {
        completeExceptionally(t);
        this.request = null;
    }

    @Override
    public boolean cancel(boolean mayInterrupt) {
        return super.cancel(mayInterrupt);
    }
}
