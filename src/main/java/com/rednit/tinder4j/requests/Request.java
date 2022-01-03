package com.rednit.tinder4j.requests;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.exceptions.HttpStatusCodeException;
import com.rednit.tinder4j.requests.async.CallbackContext;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.async.RestActionImpl;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class Request<T> {

    private static final Logger log = LoggerFactory.getLogger(RestAction.class);
    private final TinderClient client;
    private final RestActionImpl<T> restAction;
    private final Consumer<? super T> onSuccess;
    private final Consumer<? super Throwable> onFailure;
    private final Route.CompiledRoute route;
    private final RequestBody body;

    public Request(RestActionImpl<T> restAction,
                   Consumer<? super T> onSuccess,
                   Consumer<? super Throwable> onFailure,
                   Route.CompiledRoute route,
                   RequestBody body) {
        this.client = restAction.getTinderClient();
        this.restAction = restAction;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.route = route;
        this.body = body;
    }

    public void onSuccess(T successObject) {
        client.getCallbackPool().execute(() -> {
            try (CallbackContext ignored = CallbackContext.getInstance()) {
                onSuccess.accept(successObject);
            } catch (Throwable t) {
                onFailure(t);
            }
        });
    }

    public void onFailure(Response response) {
        onFailure.accept(new HttpStatusCodeException("Http response returned status code " + response.getCode()));
    }

    public void onFailure(Throwable throwable) {
        client.getCallbackPool().execute(() -> {
            try (CallbackContext ignored = CallbackContext.getInstance()) {
                onFailure.accept(throwable);
            } catch (Throwable t) {
                log.error("Encountered error while processing failure consumer!", t);
            }
        });
    }

    public RestActionImpl<T> getRestAction() {
        return restAction;
    }

    public Route.CompiledRoute getCompiledRoute() {
        return route;
    }

    public RequestBody getBody() {
        return body;
    }

    public void handleResponse(Response response) {
        restAction.handleResponse(response, this);
    }

    public TinderClient getClient() {
        return restAction.getTinderClient();
    }

}
