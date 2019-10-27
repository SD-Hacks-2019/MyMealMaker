package com.example.mymealmaker;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Serializable serialized = getArguments().getSerializable(ARG_ITEM_ID);
            if (serialized instanceof SerializableRecipe) {
                try {
                    mItem = new Recipe((SerializableRecipe) serialized);
                }
                catch (JSONException jse) {
                    jse.printStackTrace();
                }
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                try {
                    appBarLayout.setTitle(mItem.getRecipe().getString("label"));
                }
                catch (JSONException jse) {
                    jse.printStackTrace();
                }
            }
        }
    }

    public void appendNutritionalQuantity(StringBuilder builder, String label, JSONObject nutrients) throws JSONException {
        JSONObject object = nutrients.getJSONObject(label);
        builder.append(object.getString("label"));
        builder.append(": ");
        builder.append((int)object.getDouble("quantity"));
        builder.append(object.getString("unit"));
        builder.append('\n');
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            StringBuilder details = new StringBuilder();
            try {
                JSONObject recipe = mItem.getRecipe();
                JSONObject nutrients = recipe.getJSONObject("totalNutrients");
                details.append("Total Time: ");
                int time = recipe.getInt("totalTime");
                details.append((time != 0) ? time : 20);
                details.append(" min\n");
                details.append("Weight: ");
                details.append((int)recipe.getDouble("totalWeight"));
                details.append("g\n");
                details.append("Yield: ");
                details.append(recipe.getInt("yield"));
                details.append("\n\n");

                details.append("Ingredients:\n\n");
                JSONArray ingredientArray = recipe.getJSONArray("ingredientLines");
                for (int i = 0; i < ingredientArray.length(); i++) {
                    details.append(ingredientArray.getString(i));
                    details.append('\n');
                }

                details.append("\nNutritional Information:\n");

                details.append("Calories: ");
                details.append((int)recipe.getDouble("calories"));
                details.append('\n');

                appendNutritionalQuantity(details, "FAT", nutrients);
                appendNutritionalQuantity(details, "SUGAR", nutrients);
                appendNutritionalQuantity(details, "NA", nutrients);
                appendNutritionalQuantity(details, "CHOCDF", nutrients);
                appendNutritionalQuantity(details, "FIBTG", nutrients);
                appendNutritionalQuantity(details, "PROCNT", nutrients);
                appendNutritionalQuantity(details, "CHOLE", nutrients);

            }
            catch (JSONException jse) {
                jse.printStackTrace();
            }
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setTextSize(16.0f);
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(details);
        }

        return rootView;
    }
}
