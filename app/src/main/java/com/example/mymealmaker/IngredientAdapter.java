package com.example.mymealmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    Context context;
    LayoutInflater layoutInflater = null;
    private ArrayList<Ingredient> ingredientList;

    public IngredientAdapter(ArrayList<Ingredient> data, Context context) {
        super(context, R.layout.ingredient_row, data);
        this.context = context;
        this.ingredientList = data;
    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public long getItemId(int position) {
        return ingredientList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.ingredient_row, null);
        ((TextView)convertView.findViewById(R.id.name)).setText(ingredientList.get(position).getLabel().getName());
        return convertView;
    }
}
