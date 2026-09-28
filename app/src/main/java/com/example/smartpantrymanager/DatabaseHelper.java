package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 3;

    // =========================
    // PANTRY TABLE
    // =========================

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // =========================
    // RECIPES TABLE
    // =========================

    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_METHOD = "method";

    // =========================
    // RECIPE INGREDIENTS TABLE
    // =========================

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String RECIPE_INGREDIENT_ID = "id";
    public static final String RECIPE_INGREDIENT_RECIPE_ID =
            "recipe_id";
    public static final String RECIPE_INGREDIENT_NAME =
            "ingredient_name";
    public static final String RECIPE_INGREDIENT_QUANTITY =
            "required_quantity";
    public static final String RECIPE_INGREDIENT_UNIT =
            "unit";

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =========================
    // CREATE DATABASE
    // =========================

    @Override
    public void onCreate(SQLiteDatabase db) {

        createPantryTable(db);
        createRecipeTables(db);

        seedRecipes(db);
    }

    // =========================
    // PANTRY TABLE
    // =========================

    private void createPantryTable(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +

                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_QUANTITY +
                        " REAL NOT NULL, " +

                        COLUMN_UNIT +
                        " TEXT NOT NULL, " +

                        COLUMN_EXPIRY_DATE +
                        " TEXT NOT NULL" +

                        ")";

        db.execSQL(createPantryTable);
    }

    // =========================
    // RECIPE TABLES
    // =========================

    private void createRecipeTables(SQLiteDatabase db) {

        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +

                        RECIPE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RECIPE_NAME +
                        " TEXT NOT NULL, " +

                        RECIPE_METHOD +
                        " TEXT NOT NULL" +

                        ")";

        db.execSQL(createRecipesTable);

        String createRecipeIngredientsTable =
                "CREATE TABLE " +
                        TABLE_RECIPE_INGREDIENTS + " (" +

                        RECIPE_INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RECIPE_INGREDIENT_RECIPE_ID +
                        " INTEGER NOT NULL, " +

                        RECIPE_INGREDIENT_NAME +
                        " TEXT NOT NULL, " +

                        RECIPE_INGREDIENT_QUANTITY +
                        " REAL NOT NULL, " +

                        RECIPE_INGREDIENT_UNIT +
                        " TEXT NOT NULL, " +

                        "FOREIGN KEY (" +
                        RECIPE_INGREDIENT_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" + RECIPE_ID + ")" +

                        ")";

        db.execSQL(createRecipeIngredientsTable);
    }

    // =========================
    // DATABASE UPGRADE
    // =========================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        if (oldVersion < 2) {
            // Version 2 was used for pantry CRUD.
            // Existing pantry data is preserved.
        }

        if (oldVersion < 3) {

            createRecipeTables(db);

            seedRecipes(db);
        }
    }

    // =========================
    // ADD INGREDIENT
    // =========================

    public long addIngredient(
            String name,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                name
        );

        values.put(
                COLUMN_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_UNIT,
                unit
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                expiryDate
        );

        long result =
                db.insert(
                        TABLE_PANTRY,
                        null,
                        values
                );

        db.close();

        return result;
    }

    // =========================
    // GET ALL INGREDIENTS
    // =========================

    public android.database.Cursor getAllIngredients() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }

    // =========================
    // UPDATE INGREDIENT
    // =========================

    public int updateIngredient(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                name
        );

        values.put(
                COLUMN_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_UNIT,
                unit
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                expiryDate
        );

        int result =
                db.update(
                        TABLE_PANTRY,
                        values,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }

    // =========================
    // DELETE INGREDIENT
    // =========================

    public int deleteIngredient(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_PANTRY,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }

    // =========================
    // SEED RECIPES
    // =========================

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Tomato Omelette",
                "Beat the eggs. Add tomatoes and seasoning. Cook in a pan until the eggs are set.",
                new String[]{"tomatoes", "eggs"},
                new double[]{2, 2},
                new String[]{"pieces", "pieces"}
        );

        addRecipe(
                db,
                "Scrambled Eggs",
                "Beat the eggs and cook them gently in a pan until soft and fully cooked.",
                new String[]{"eggs", "milk"},
                new double[]{2, 50},
                new String[]{"pieces", "ml"}
        );

        addRecipe(
                db,
                "Tomato Pasta",
                "Cook the pasta. Prepare a tomato sauce and combine with the cooked pasta.",
                new String[]{"tomatoes", "pasta", "onion"},
                new double[]{2, 200, 1},
                new String[]{"pieces", "g", "pieces"}
        );

        addRecipe(
                db,
                "Chicken Pasta",
                "Cook the pasta and chicken separately. Combine with seasoning and serve.",
                new String[]{"chicken", "pasta"},
                new double[]{250, 200},
                new String[]{"g", "g"}
        );

        addRecipe(
                db,
                "Chicken Rice",
                "Cook the rice. Fry the chicken until cooked and combine with the rice.",
                new String[]{"chicken", "rice"},
                new double[]{250, 200},
                new String[]{"g", "g"}
        );

        addRecipe(
                db,
                "Vegetable Rice",
                "Cook the rice and fry the vegetables. Mix everything together.",
                new String[]{"rice", "carrots", "peas"},
                new double[]{200, 2, 100},
                new String[]{"g", "pieces", "g"}
        );

        addRecipe(
                db,
                "Cheese Omelette",
                "Beat the eggs, add cheese and cook in a pan until golden.",
                new String[]{"eggs", "cheese"},
                new double[]{2, 50},
                new String[]{"pieces", "g"}
        );

        addRecipe(
                db,
                "Chicken Sandwich",
                "Place cooked chicken, lettuce and tomato between slices of bread.",
                new String[]{"bread", "chicken", "lettuce", "tomatoes"},
                new double[]{2, 150, 50, 1},
                new String[]{"pieces", "g", "g", "pieces"}
        );

        addRecipe(
                db,
                "Cheese Sandwich",
                "Place cheese between slices of bread and toast until warm.",
                new String[]{"bread", "cheese"},
                new double[]{2, 50},
                new String[]{"pieces", "g"}
        );

        addRecipe(
                db,
                "French Toast",
                "Dip bread in beaten eggs and milk, then fry until golden.",
                new String[]{"bread", "eggs", "milk"},
                new double[]{2, 2, 50},
                new String[]{"pieces", "pieces", "ml"}
        );

        addRecipe(
                db,
                "Tomato Salad",
                "Chop tomatoes and cucumber. Mix together and season.",
                new String[]{"tomatoes", "cucumber"},
                new double[]{2, 1},
                new String[]{"pieces", "pieces"}
        );

        addRecipe(
                db,
                "Chicken Salad",
                "Combine cooked chicken with lettuce and cucumber.",
                new String[]{"chicken", "lettuce", "cucumber"},
                new double[]{150, 50, 1},
                new String[]{"g", "g", "pieces"}
        );

        addRecipe(
                db,
                "Vegetable Omelette",
                "Beat the eggs and add chopped vegetables. Cook until fully set.",
                new String[]{"eggs", "tomatoes", "onion"},
                new double[]{2, 1, 1},
                new String[]{"pieces", "pieces", "pieces"}
        );

        addRecipe(
                db,
                "Potato Omelette",
                "Cook the potatoes, add beaten eggs and fry until set.",
                new String[]{"potatoes", "eggs"},
                new double[]{2, 2},
                new String[]{"pieces", "pieces"}
        );

        addRecipe(
                db,
                "Chicken Wrap",
                "Fill a wrap with cooked chicken, lettuce and tomato.",
                new String[]{"wraps", "chicken", "lettuce", "tomatoes"},
                new double[]{1, 150, 50, 1},
                new String[]{"pieces", "g", "g", "pieces"}
        );

        addRecipe(
                db,
                "Egg Sandwich",
                "Boil the eggs, slice them and place them between bread.",
                new String[]{"bread", "eggs"},
                new double[]{2, 2},
                new String[]{"pieces", "pieces"}
        );

        addRecipe(
                db,
                "Tomato Soup",
                "Cook tomatoes with onion and water. Blend until smooth and serve hot.",
                new String[]{"tomatoes", "onion", "water"},
                new double[]{4, 1, 500},
                new String[]{"pieces", "pieces", "ml"}
        );

        addRecipe(
                db,
                "Chicken Stir Fry",
                "Fry chicken with carrots and peppers until everything is cooked.",
                new String[]{"chicken", "carrots", "peppers"},
                new double[]{250, 1, 1},
                new String[]{"g", "pieces", "pieces"}
        );

        addRecipe(
                db,
                "Vegetable Pasta",
                "Cook pasta and combine with cooked vegetables.",
                new String[]{"pasta", "carrots", "peas"},
                new double[]{200, 1, 100},
                new String[]{"g", "pieces", "g"}
        );

        addRecipe(
                db,
                "Egg Fried Rice",
                "Fry cooked rice with eggs and vegetables until hot.",
                new String[]{"rice", "eggs", "peas"},
                new double[]{200, 2, 100},
                new String[]{"g", "pieces", "g"}
        );

        addRecipe(
                db,
                "Chicken Tomato Pasta",
                "Cook pasta and chicken. Add tomatoes and combine everything.",
                new String[]{"chicken", "pasta", "tomatoes"},
                new double[]{200, 200, 2},
                new String[]{"g", "g", "pieces"}
        );
    }

    // =========================
    // ADD RECIPE
    // =========================

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String method,
            String[] ingredients,
            double[] quantities,
            String[] units) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                RECIPE_NAME,
                name
        );

        recipeValues.put(
                RECIPE_METHOD,
                method
        );

        long recipeId =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        recipeValues
                );

        if (recipeId == -1) {
            return;
        }

        for (int i = 0; i < ingredients.length; i++) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    RECIPE_INGREDIENT_RECIPE_ID,
                    recipeId
            );

            ingredientValues.put(
                    RECIPE_INGREDIENT_NAME,
                    ingredients[i]
            );

            ingredientValues.put(
                    RECIPE_INGREDIENT_QUANTITY,
                    quantities[i]
            );

            ingredientValues.put(
                    RECIPE_INGREDIENT_UNIT,
                    units[i]
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }
    // GET ALL RECIPES
    public List<RecipeItem> getAllRecipes() {

        List<RecipeItem> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor recipeCursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_ID + " ASC"
        );

        if (recipeCursor != null) {

            int idIndex =
                    recipeCursor.getColumnIndexOrThrow(RECIPE_ID);

            int nameIndex =
                    recipeCursor.getColumnIndexOrThrow(RECIPE_NAME);

            int methodIndex =
                    recipeCursor.getColumnIndexOrThrow(RECIPE_METHOD);

            while (recipeCursor.moveToNext()) {

                int recipeId = recipeCursor.getInt(idIndex);
                String recipeName = recipeCursor.getString(nameIndex);
                String method = recipeCursor.getString(methodIndex);

                List<RecipeIngredient> ingredients =
                        getRecipeIngredients(recipeId);

                recipes.add(
                        new RecipeItem(
                                recipeId,
                                recipeName,
                                method,
                                ingredients
                        )
                );
            }

            recipeCursor.close();
        }

        return recipes;
    }


    // GET INGREDIENTS FOR ONE RECIPE
    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {

        List<RecipeIngredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RECIPE_INGREDIENT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                RECIPE_INGREDIENT_ID + " ASC"
        );

        if (cursor != null) {

            int nameIndex =
                    cursor.getColumnIndexOrThrow(
                            RECIPE_INGREDIENT_NAME
                    );

            int quantityIndex =
                    cursor.getColumnIndexOrThrow(
                            RECIPE_INGREDIENT_QUANTITY
                    );

            int unitIndex =
                    cursor.getColumnIndexOrThrow(
                            RECIPE_INGREDIENT_UNIT
                    );

            while (cursor.moveToNext()) {

                ingredients.add(
                        new RecipeIngredient(
                                cursor.getString(nameIndex),
                                cursor.getDouble(quantityIndex),
                                cursor.getString(unitIndex)
                        )
                );
            }

            cursor.close();
        }

        return ingredients;
    }


    // GET RECIPES THE USER CAN ACTUALLY MAKE
    public List<RecipeItem> getSuggestedRecipes() {

        List<RecipeItem> suggestedRecipes = new ArrayList<>();

        List<RecipeItem> allRecipes = getAllRecipes();

        Map<String, List<PantryItem>> pantryMap =
                buildPantryMap();

        for (RecipeItem recipe : allRecipes) {

            boolean matches = true;

            for (RecipeIngredient required :
                    recipe.getIngredients()) {

                String requiredName =
                        normalizeIngredientName(
                                required.getName()
                        );

                double availableQuantity = 0;

                List<PantryItem> pantryItems =
                        pantryMap.get(requiredName);

                if (pantryItems == null ||
                        pantryItems.isEmpty()) {

                    matches = false;
                    break;
                }

                for (PantryItem pantryItem : pantryItems) {

                    double convertedQuantity =
                            convertToBaseUnit(
                                    pantryItem.getQuantity(),
                                    pantryItem.getUnit(),
                                    required.getUnit()
                            );

                    if (convertedQuantity >= 0) {
                        availableQuantity += convertedQuantity;
                    }
                }

                double requiredQuantity =
                        required.getQuantity();

                if (availableQuantity < requiredQuantity) {

                    matches = false;
                    break;
                }
            }

            if (matches) {
                suggestedRecipes.add(recipe);
            }
        }

        return suggestedRecipes;
    }


    // BUILD A MAP OF PANTRY INGREDIENTS
    private Map<String, List<PantryItem>> buildPantryMap() {

        Map<String, List<PantryItem>> pantryMap =
                new HashMap<>();

        Cursor cursor = getAllIngredients();

        if (cursor != null) {

            int idIndex =
                    cursor.getColumnIndexOrThrow(
                            COLUMN_ID
                    );

            int nameIndex =
                    cursor.getColumnIndexOrThrow(
                            COLUMN_NAME
                    );

            int quantityIndex =
                    cursor.getColumnIndexOrThrow(
                            COLUMN_QUANTITY
                    );

            int unitIndex =
                    cursor.getColumnIndexOrThrow(
                            COLUMN_UNIT
                    );

            int expiryIndex =
                    cursor.getColumnIndexOrThrow(
                            COLUMN_EXPIRY_DATE
                    );

            while (cursor.moveToNext()) {

                PantryItem item = new PantryItem(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getDouble(quantityIndex),
                        cursor.getString(unitIndex),
                        cursor.getString(expiryIndex)
                );

                String normalizedName =
                        normalizeIngredientName(
                                item.getName()
                        );

                if (!pantryMap.containsKey(normalizedName)) {

                    pantryMap.put(
                            normalizedName,
                            new ArrayList<>()
                    );
                }

                pantryMap.get(normalizedName).add(item);
            }

            cursor.close();
        }

        return pantryMap;
    }


    // NORMALIZE SIMPLE SINGULAR / PLURAL NAMES
    private String normalizeIngredientName(String name) {

        if (name == null) {
            return "";
        }

        String value = name
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", " ");

        if (value.equals("tomatoes")) {
            return "tomato";
        }

        if (value.equals("potatoes")) {
            return "potato";
        }

        if (value.equals("eggs")) {
            return "egg";
        }

        if (value.equals("onions")) {
            return "onion";
        }

        if (value.equals("carrots")) {
            return "carrot";
        }

        if (value.equals("peppers")) {
            return "pepper";
        }

        if (value.equals("wraps")) {
            return "wrap";
        }

        if (value.endsWith("s") &&
                !value.endsWith("ss")) {

            value = value.substring(
                    0,
                    value.length() - 1
            );
        }

        return value;
    }


    // CONVERT UNITS FOR FAIR QUANTITY COMPARISON
    private double convertToBaseUnit(
            double quantity,
            String pantryUnit,
            String requiredUnit) {

        if (pantryUnit == null ||
                requiredUnit == null) {

            return -1;
        }

        String pantry =
                pantryUnit.trim().toLowerCase();

        String required =
                requiredUnit.trim().toLowerCase();


        // GRAMS
        if (required.equals("g")) {

            if (pantry.equals("g")) {
                return quantity;
            }

            if (pantry.equals("kg")) {
                return quantity * 1000;
            }

            return -1;
        }


        // KILOGRAMS
        if (required.equals("kg")) {

            if (pantry.equals("kg")) {
                return quantity;
            }

            if (pantry.equals("g")) {
                return quantity / 1000;
            }

            return -1;
        }


        // MILLILITRES
        if (required.equals("ml")) {

            if (pantry.equals("ml")) {
                return quantity;
            }

            if (pantry.equals("litres")) {
                return quantity * 1000;
            }

            return -1;
        }


        // LITRES
        if (required.equals("litres")) {

            if (pantry.equals("litres")) {
                return quantity;
            }

            if (pantry.equals("ml")) {
                return quantity / 1000;
            }

            return -1;
        }


        // PIECES
        if (required.equals("pieces")) {

            if (pantry.equals("pieces")) {
                return quantity;
            }

            return -1;
        }


        // PACKETS
        if (required.equals("packets")) {

            if (pantry.equals("packets")) {
                return quantity;
            }

            return -1;
        }


        // UNKNOWN UNIT
        if (pantry.equals(required)) {
            return quantity;
        }

        return -1;
    }
    // COUNT INGREDIENTS EXPIRING WITHIN 7 DAYS
    public int getExpiringSoonCount() {

        int count = 0;

        Cursor cursor = getAllIngredients();

        if (cursor != null) {

            int expiryIndex =
                    cursor.getColumnIndexOrThrow(
                            COLUMN_EXPIRY_DATE
                    );

            java.text.SimpleDateFormat dateFormat =
                    new java.text.SimpleDateFormat(
                            "dd/MM/yyyy",
                            java.util.Locale.getDefault()
                    );

            dateFormat.setLenient(false);

            java.util.Calendar today =
                    java.util.Calendar.getInstance();

            today.set(
                    java.util.Calendar.HOUR_OF_DAY,
                    0
            );
            today.set(
                    java.util.Calendar.MINUTE,
                    0
            );
            today.set(
                    java.util.Calendar.SECOND,
                    0
            );
            today.set(
                    java.util.Calendar.MILLISECOND,
                    0
            );

            java.util.Calendar sevenDaysLater =
                    (java.util.Calendar) today.clone();

            sevenDaysLater.add(
                    java.util.Calendar.DAY_OF_YEAR,
                    7
            );

            while (cursor.moveToNext()) {

                String expiryDate =
                        cursor.getString(expiryIndex);

                if (expiryDate == null ||
                        expiryDate.trim().isEmpty()) {
                    continue;
                }

                try {

                    java.util.Date expiry =
                            dateFormat.parse(
                                    expiryDate.trim()
                            );

                    if (expiry != null &&
                            !expiry.before(today.getTime()) &&
                            !expiry.after(sevenDaysLater.getTime())) {

                        count++;
                    }

                } catch (java.text.ParseException e) {
                    // Ignore invalid or empty expiry dates
                }
            }

            cursor.close();
        }

        return count;
    }
}
