package com.example.mymealmaker;

import android.app.Activity;
import android.util.JsonReader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EdamamCallback implements Callback {
    Activity activity;

    public EdamamCallback(Activity activity) {
        this.activity = activity;
    }

    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        e.printStackTrace();
    }

    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.isSuccessful()) {
            try {
                JSONObject responseJson = new JSONObject(response.body().string());
                JSONArray recipes = responseJson.getJSONArray("hits");

                List<Recipe> recipeList = new ArrayList<>();

                for (int i = 0; i < recipes.length(); i++) {
                    JSONObject current = recipes.getJSONObject(i).getJSONObject("recipe");

                    double weight = current.getDouble("totalWeight");

                    JSONObject nutrients = current.getJSONObject("totalNutrients");
                    double satFat = nutrients.getJSONObject("FASAT").getDouble("quantity");
                    double transFat = nutrients.getJSONObject("FATRN").getDouble("quantity");
                    double totalUnhealthyFat = satFat + transFat;
                    double healthMetric = totalUnhealthyFat / weight;

                    recipeList.add(new Recipe(current, healthMetric));
                }


            }
            catch (JSONException jse) {
                jse.printStackTrace();
            }
        }
    }
}
