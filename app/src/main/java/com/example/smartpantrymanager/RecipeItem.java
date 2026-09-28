package com.example.smartpantrymanager;

import java.util.List;

public class RecipeItem {

    private int id;
    private String name;
    private String method;
    private List<RecipeIngredient> ingredients;

    public RecipeItem(int id, String name, String method,
                      List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.method = method;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }
}