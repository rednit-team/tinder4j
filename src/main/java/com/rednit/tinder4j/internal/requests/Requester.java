package com.rednit.tinder4j.internal.requests;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

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
        try {
            okhttp3.Response response = httpClient.newCall(builder.build()).execute();
            request.handleResponse(new Response(response));
        } catch (IOException e) {
            request.onFailure(e);
        }
    }
}
