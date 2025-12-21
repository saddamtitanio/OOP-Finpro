package com.finpro.frontend.backendservice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

public class BackEndService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private String username;

    public interface RequestCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public BackEndService(String username) {
        this.username = username;
    }

    public void submitFinalScore(int finalScore, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"score\":" + finalScore + "}";

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/scores")
            .header("Content-Type", "application/json")
            .content(json)
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                callback.onSuccess(httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                callback.onError(t.getMessage());
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }
}
