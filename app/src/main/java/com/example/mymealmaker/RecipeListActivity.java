package com.example.mymealmaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<Recipe> ingredients = new ArrayList<>();
    View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey(MainActivity.INGREDIENTS)) {
                Serializable incoming = extras.getSerializable(MainActivity.INGREDIENTS);
                List<String> incomingList = new ArrayList<>();
                if (incoming instanceof List) {
                    incomingList = (List<String>) incoming;
                }


                EdamamCallback myCallback = new EdamamCallback(this);
                EdamamClient myEdamamClient = new EdamamClient(MainActivity.edamamID, MainActivity.edamamSecret);
                myEdamamClient.requestRecipe(incomingList, myCallback);
            }
            else if (extras.containsKey(RecipeDetailActivity.RECIPES)) {
                Serializable incoming = extras.getSerializable(RecipeDetailActivity.RECIPES);
                List<SerializableRecipe> incomingList = new ArrayList<>();
                if (incoming instanceof List) {
                    incomingList = (List<SerializableRecipe>) incoming;
                }

                List<Recipe> deserialized = new ArrayList<Recipe>();
                for (SerializableRecipe recipe : incomingList) {
                    try {
                        deserialized.add(new Recipe(recipe));
                    }
                    catch (JSONException jse) {
                        jse.printStackTrace();
                    }
                }

                ingredients = deserialized;
            }
        }

        // set up the recycler (list) view
        recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }


    public void setIngredients(List<Recipe> ingredients, Object caller) {
        if (caller instanceof EdamamCallback) {
            this.ingredients = ingredients;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRecyclerView((RecyclerView) recyclerView);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ingredients, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Recipe> mValues;
        private final boolean mTwoPane;

        public void clickHandler(View view) {
            Recipe item = (Recipe) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                try {
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, item.getRecipe().getString("label"));
                }
                catch (JSONException jse) {
                    jse.printStackTrace();
                }
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);

                intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, new SerializableRecipe(item));

                List<SerializableRecipe> serialized = new ArrayList<>();
                for (Recipe recipe : mValues) {
                    serialized.add(new SerializableRecipe(recipe));
                }

                intent.putExtra(RecipeDetailActivity.RECIPES, (Serializable) serialized);

                context.startActivity(intent);
            }
        }

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      List<Recipe> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                holder.mNameView.setText(mValues.get(position).getRecipe().getString("label"));
                holder.mThumbView.loadUrl(mValues.get(position).getRecipe().getString("image"));
                System.out.println(mValues.get(position).getRecipe().getString("image"));
                //holder.mThumbView.setImageURI(Uri.parse(mValues.get(position).getRecipe().getString("image")));
                //holder.mThumbView.setText(mValues.get(position).getRecipe().getString("source"));
            }
            catch (JSONException jse) {
                jse.printStackTrace();
            }

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;
            final WebView mThumbView;

            ViewHolder(View view) {
                super(view);
                mNameView = (TextView) view.findViewById(R.id.id_text);
                mThumbView = (WebView) view.findViewById(R.id.content);
            }
        }
    }
}
