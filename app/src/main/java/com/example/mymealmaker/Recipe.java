package com.example.mymealmaker;

import org.json.JSONObject;

public class Recipe {
    private JSONObject recipe;
    private double healthMetric;

    public Recipe(JSONObject recipe, double healthMetric) {
        this.recipe = recipe;
        this.healthMetric = healthMetric;
    }

    public JSONObject getRecipe() {
        return recipe;
    }

    public double getHealthMetric() {
        return healthMetric;
    }
}
