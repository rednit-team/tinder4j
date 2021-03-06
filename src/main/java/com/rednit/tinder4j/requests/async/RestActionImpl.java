package com.rednit.tinder4j.requests.async;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.Request;
import com.rednit.tinder4j.requests.Response;
import com.rednit.tinder4j.requests.Route;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class RestActionImpl<T> implements RestAction<T> {

    public static final Logger log = LoggerFactory.getLogger(RestAction.class);
    private static final Consumer<Object> DEFAULT_SUCCESS = o -> {
    };
    private static final Consumer<? super Throwable> DEFAULT_FAILURE = t -> log.error("RestAction queue returned failure", t);
    private static final RequestBody EMPTY_BODY = RequestBody.create("{}", MediaType.parse("application/json"));
    private final TinderClient client;
    private final Route.CompiledRoute route;
    private final RequestBody data;
    private final BiFunction<Response, Request<T>, T> handler;

    public RestActionImpl(TinderClient client, Route.CompiledRoute route) {
        this(client, route, EMPTY_BODY, null);
    }

    public RestActionImpl(TinderClient client, Route.CompiledRoute route, RequestBody data) {
        this(client, route, data, null);
    }

    public RestActionImpl(TinderClient client, Route.CompiledRoute route, BiFunction<Response, Request<T>, T> handler) {
        this(client, route, EMPTY_BODY, handler);
    }

    public RestActionImpl(TinderClient client, Route.CompiledRoute route, RequestBody data, BiFunction<Response, Request<T>, T> handler) {
        this.client = client;
        this.route = route;
        this.data = data;
        this.handler = handler;
    }

    public TinderClient getTinderClient() {
        return client;
    }

    @Override
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
        if (success == null) {
            success = DEFAULT_SUCCESS;
        }
        if (failure == null) {
            failure = DEFAULT_FAILURE;
        }
        client.getRequester().request(new Request<>(this, success, failure, route, data));
    }

    @Override
    public T complete() {
        if (CallbackContext.isCallbackContext()) {
            throw new IllegalStateException("Preventing use of complete() in callback threads! This operation can be a deadlock cause!");
        }
        CompletableFuture<T> future = new CompletableFuture<>();
        client.getRequester().request(new Request<>(
                this, future::complete, future::completeExceptionally, route, data
        ));
        return future.join();
    }

    public void handleResponse(Response response, Request<T> request) {
        if (response.isOk()) {
            if (handler == null) {
                request.onSuccess(null);
            } else {
                request.onSuccess(handler.apply(response, request));
            }
        } else {
            request.onFailure(response);
        }
        response.close();
    }
}
