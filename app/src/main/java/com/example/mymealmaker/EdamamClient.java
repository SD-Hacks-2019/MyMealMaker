package com.example.mymealmaker;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EdamamClient {
    private static final String EDAMAM_URL = "https://api.edamam.com/search";

    private String appID;
    private String appKey;
    private OkHttpClient client;

    public EdamamClient(String appID, String appKey) {
        this.appID = appID;
        this.appKey = appKey;
        client = new OkHttpClient();
    }

    public void requestRecipe(List<String> ingredients, Callback onCompletion) {
        StringBuilder url = new StringBuilder(EDAMAM_URL);
        url.append("?q=");

        StringBuilder query = new StringBuilder();

        for (String ingredient : ingredients) {
            query.append(ingredient);
            query.append('+');
        }

        if (query.charAt(query.length() - 1) == '+') {
            query.deleteCharAt(query.length() - 1);
        }

        url.append(query);

        url.append("&app_id=");
        url.append(appID);

        url.append("&app_key=");
        url.append(appKey);

        final Request request = new Request.Builder()
                .url(url.toString())
                .build();

        client.newCall(request).enqueue(onCompletion);
    }

}
