package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvDetailRecipeName;
    private TextView tvDetailIngredients;
    private TextView tvDetailMethod;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvDetailRecipeName = findViewById(R.id.tvDetailRecipeName);
        tvDetailIngredients = findViewById(R.id.tvDetailIngredients);
        tvDetailMethod = findViewById(R.id.tvDetailMethod);

        TextView btnBackRecipeDetail =
                findViewById(R.id.btnBackRecipeDetail);

        databaseHelper = new DatabaseHelper(this);

        int recipeId =
                getIntent().getIntExtra("recipe_id", -1);

        String recipeName =
                getIntent().getStringExtra("recipe_name");

        String recipeMethod =
                getIntent().getStringExtra("recipe_method");

        tvDetailRecipeName.setText(recipeName);
        tvDetailMethod.setText(recipeMethod);

        loadIngredients(recipeId);

        btnBackRecipeDetail.setOnClickListener(v -> finish());
    }

    private void loadIngredients(int recipeId) {

        List<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            double quantity = ingredient.getQuantity();

            String quantityText;

            if (quantity == Math.floor(quantity)) {
                quantityText = String.format("%.0f", quantity);
            } else {
                quantityText = String.valueOf(quantity);
            }

            ingredientText
                    .append("• ")
                    .append(quantityText)
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getName())
                    .append("\n");
        }

        tvDetailIngredients.setText(
                ingredientText.toString().trim()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}