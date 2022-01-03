package com.rednit.tinder4j.requests;

import com.rednit.tinder4j.api.requests.Ratelimiter;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Requester {

    private final static Logger log = LoggerFactory.getLogger(Requester.class);
    private final OkHttpClient httpClient;
    private final Headers headers;
    private final String baseURL;

    public Requester(String token) {
        this.httpClient = new OkHttpClient();
        headers = Headers.of(new HashMap<>() {{
            put(
                    "User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36"
            );
            put("Content-Type", "application/json");
            put("X-Auth-Token", token);
        }});
        baseURL = "https://api.gotinder.com";
    }

    public <T> void request(Request<T> request) {
        String url = baseURL + request.getCompiledRoute().getRoute();
        Route.Method method = request.getCompiledRoute().getMethod();
        log.debug("Sending {} request to {}", method.name(), url);

        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .headers(headers)
                .url(url);

        switch (method) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(request.getBody());
                break;
            case DELETE:
                builder.delete();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported request method!");
        }

        Ratelimiter ratelimiter = request.getClient().getRatelimiter();
        if (ratelimiter.shouldDelay(request)) {
            synchronized (this) {
                try {
                    long timeout = ratelimiter.getDelay(request);
                    log.debug("Too many requests. Waiting for {} ms", timeout);
                    wait(timeout);
                    log.debug("Continuing...");
                } catch (InterruptedException e) {
                    log.error("An exception occurred while rate limiting!", e);
                }
            }
        }

        Response response;
        try {
            okhttp3.Response data = httpClient.newCall(builder.build()).execute();
            response = new Response(data);
        } catch (IOException e) {
            request.onFailure(e);
            return;
        }
        if (response.isOk()) {
            request.handleResponse(response);
        } else if (response.isRateLimit()) {
            synchronized (this) {
                try {
                    int timeout = (int) (ThreadLocalRandom.current().nextFloat() * 10 + 1);
                    log.debug("Too many requests. Waiting for {} secs", timeout);
                    wait(timeout * 1000L);
                    log.debug("Reattempting...");
                    request(request);
                } catch (InterruptedException e) {
                    log.error("An exception occurred while rate limiting!", e);
                }
            }
        } else {
            request.onFailure(response);
        }
    }
}
