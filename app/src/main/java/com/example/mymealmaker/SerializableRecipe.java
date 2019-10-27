package com.example.mymealmaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SerializableRecipe implements Serializable {
    public String recipe;
    public double healthMetric;

    public SerializableRecipe() {
        recipe = "";
        healthMetric = 0.0;
    }

    public SerializableRecipe(Recipe recipe) {
        this.recipe = recipe.getRecipe().toString();
        this.healthMetric = recipe.getHealthMetric();
    }

    public JSONObject desearalizeRecipe() throws JSONException {
        return new JSONObject(recipe);
    }
}
