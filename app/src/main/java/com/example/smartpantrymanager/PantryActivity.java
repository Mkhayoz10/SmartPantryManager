package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PantryAdapter pantryAdapter;
    private List<PantryItem> pantryItems;

    private DatabaseHelper databaseHelper;

    private View emptyState;
    private TextView pantryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        // Find views from activity_pantry.xml
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        emptyState = findViewById(R.id.emptyState);
        pantryCount = findViewById(R.id.pantryCount);

        // Database
        databaseHelper = new DatabaseHelper(this);

        // Pantry list
        pantryItems = new ArrayList<>();

        pantryAdapter = new PantryAdapter(pantryItems);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(pantryAdapter);

        // Add Ingredient button
        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddIngredientActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPantryItems();
    }

    private void loadPantryItems() {

        pantryItems.clear();

        Cursor cursor = databaseHelper.getAllIngredients();

        if (cursor != null) {

            int idIndex =
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_ID
                    );

            int nameIndex =
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_NAME
                    );

            int quantityIndex =
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_QUANTITY
                    );

            int unitIndex =
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_UNIT
                    );

            int expiryIndex =
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_EXPIRY_DATE
                    );

            while (cursor.moveToNext()) {

                PantryItem item = new PantryItem(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getDouble(quantityIndex),
                        cursor.getString(unitIndex),
                        cursor.getString(expiryIndex)
                );

                pantryItems.add(item);
            }

            cursor.close();
        }

        // Update RecyclerView
        pantryAdapter.notifyDataSetChanged();

        // Update pantry counter
        String countText = pantryItems.size()
                + (pantryItems.size() == 1
                ? " ingredient"
                : " ingredients");

        pantryCount.setText(countText);

        // Show empty state or ingredient list
        if (pantryItems.isEmpty()) {

            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);

        } else {

            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
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
