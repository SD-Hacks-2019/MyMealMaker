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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            try {
                JSONObject recipe = mItem.getRecipe();
                StringBuilder details = new StringBuilder("Ingredients:\n\n");
                JSONArray ingredientArray = recipe.getJSONArray("ingredientLines");
                for (int i = 0; i < ingredientArray.length(); i++) {
                    details.append(ingredientArray.getString(i));
                    details.append('\n');
                }

                details.append("Nutritional Information:\n");

                ((TextView) rootView.findViewById(R.id.recipe_detail)).setTextSize(16.0f);
                ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(details);
            }
            catch (JSONException jse) {
                jse.printStackTrace();
            }
        }

        return rootView;
    }
}
