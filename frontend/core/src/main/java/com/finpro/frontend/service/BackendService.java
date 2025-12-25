package com.finpro.frontend.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.util.Map;
import java.util.UUID;

public class BackendService {

    private static final String BASE_URL = "https://oop-finpro-production.up.railway.app/api";

    public interface RequestCallback {
        void onSuccess(String response);
        void onError(int statusCode, String responseBody);
    }

    public void createPlayer(String username, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\"}";

        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/players")
            .header("Content-Type", "application/json")
            .content(json)
            .build();

        sendRequest(request, callback);
    }

    public void getPlayerByUsername(String username, RequestCallback callback) {
        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url(BASE_URL + "/players/username/" + username)
            .build();

        sendRequest(request, callback);
    }

    public void submitScore(String playerId, int scoreValue, RequestCallback callback) {
        String json = String.format(
            "{\"playerId\":\"%s\",\"value\":%d}",
            playerId, scoreValue
        );

        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/scores")
            .header("Content-Type", "application/json")
            .content(json)
            .build();

        sendRequest(request, callback);
    }

    public void submitRun(UUID playerId, int score, int durationSeconds, Map<String, Integer> kills,
        RequestCallback callback) {
        StringBuilder killsJson = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<String, Integer> entry : kills.entrySet()) {
            killsJson.append("\"")
                .append(entry.getKey())
                .append("\":")
                .append(entry.getValue());

            if (++i < kills.size()) {
                killsJson.append(",");
            }
        }
        killsJson.append("}");

        String json = String.format(
            "{\"playerId\":\"%s\",\"score\":%d,\"durationSeconds\":%d,\"kills\":%s}",
            playerId, score, durationSeconds, killsJson
        );

        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/runs")
            .header("Content-Type", "application/json")
            .content(json)
            .build();

        sendRequest(request, callback);
    }

    public void getAllRuns(RequestCallback callback) {
        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url(BASE_URL + "/runs")
            .build();

        sendRequest(request, callback);
    }

    public void getRunsByPlayer(String playerId, RequestCallback callback) {
        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url(BASE_URL + "/runs/player/" + playerId)
            .build();

        sendRequest(request, callback);
    }

    private void sendRequest(Net.HttpRequest request, RequestCallback callback) {
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String body = httpResponse.getResultAsString();

                if (statusCode >= 200 && statusCode < 300) {
                    callback.onSuccess(body);
                } else {
                    Gdx.app.error(
                        "BackendService",
                        "HTTP " + statusCode + ": " + body
                    );
                    callback.onError(statusCode, body);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("BackendService", "Request failed", t);
                callback.onError(-1, t.getMessage());
            }

            @Override
            public void cancelled() {
                callback.onError(-1, "Request cancelled");
            }
        });
    }

    public void getGlobalLeaderboard(RequestCallback callback) {
        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url(BASE_URL + "/players/leaderboard/global")
            .build();

        sendRequest(request, callback);
    }

    public void getTopRunsForPlayer(UUID playerId, int limit, RequestCallback callback) {
        Net.HttpRequest request = new HttpRequestBuilder()
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url(BASE_URL + "/leaderboard/" + playerId + "?limit=" + limit)
            .build();

        sendRequest(request, callback);
    }

}
