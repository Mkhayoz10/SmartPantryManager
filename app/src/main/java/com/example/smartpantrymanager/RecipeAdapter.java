package com.example.smartpantrymanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<RecipeItem> recipes;

    public RecipeAdapter(List<RecipeItem> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        RecipeItem recipe = recipes.get(position);

        holder.tvRecipeName.setText(recipe.getName());

        StringBuilder ingredientsText = new StringBuilder();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {

            if (ingredientsText.length() > 0) {
                ingredientsText.append(" • ");
            }

            double quantity = ingredient.getQuantity();

            String quantityText;

            if (quantity == Math.floor(quantity)) {
                quantityText = String.format("%.0f", quantity);
            } else {
                quantityText = String.valueOf(quantity);
            }

            ingredientsText.append(quantityText)
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getName());
        }

        holder.tvRecipeIngredients.setText(ingredientsText.toString());
        holder.tvRecipeAction.setText("Tap to view recipe →");

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    RecipeDetailActivity.class
            );

            intent.putExtra("recipe_id", recipe.getId());
            intent.putExtra("recipe_name", recipe.getName());
            intent.putExtra("recipe_method", recipe.getMethod());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeIngredients;
        TextView tvRecipeAction;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeIngredients =
                    itemView.findViewById(R.id.tvRecipeIngredients);
            tvRecipeAction =
                    itemView.findViewById(R.id.tvRecipeAction);
        }
    }
}
