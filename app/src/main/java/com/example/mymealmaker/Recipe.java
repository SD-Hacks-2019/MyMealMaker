package com.example.mymealmaker;

import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {
    private JSONObject recipe;
    private double healthMetric;

    public Recipe(JSONObject recipe, double healthMetric) {
        this.recipe = recipe;
        this.healthMetric = healthMetric;
    }

    public Recipe(SerializableRecipe recipe) throws JSONException {
        this.recipe = recipe.desearalizeRecipe();
        this.healthMetric = getHealthMetric();
    }

    public JSONObject getRecipe() {
        return recipe;
    }

    public double getHealthMetric() {
        return healthMetric;
    }
}
