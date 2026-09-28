package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    private List<RecipeItem> suggestedRecipes;

    private DatabaseHelper databaseHelper;
    private View emptyRecipeState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerViewRecipes =
                findViewById(R.id.recyclerViewRecipes);

        emptyRecipeState =
                findViewById(R.id.emptyRecipeState);

        TextView btnBackRecipes =
                findViewById(R.id.btnBackRecipes);

        databaseHelper = new DatabaseHelper(this);

        suggestedRecipes = new ArrayList<>();

        recipeAdapter =
                new RecipeAdapter(suggestedRecipes);

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewRecipes.setAdapter(recipeAdapter);

        btnBackRecipes.setOnClickListener(v -> finish());

        loadSuggestedRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadSuggestedRecipes();
        }
    }

    private void loadSuggestedRecipes() {

        suggestedRecipes.clear();

        List<RecipeItem> recipes =
                databaseHelper.getSuggestedRecipes();

        suggestedRecipes.addAll(recipes);

        recipeAdapter.notifyDataSetChanged();

        if (suggestedRecipes.isEmpty()) {

            recyclerViewRecipes.setVisibility(
                    View.GONE
            );

            emptyRecipeState.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerViewRecipes.setVisibility(
                    View.VISIBLE
            );

            emptyRecipeState.setVisibility(
                    View.GONE
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
