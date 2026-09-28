package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView homePantryCount;

    private TextView homeExpiringCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);


        homePantryCount =
                findViewById(R.id.homePantryCount);

        homeExpiringCount =
                findViewById(R.id.homeExpiringCount);

        databaseHelper =
                new DatabaseHelper(this);

        androidx.cardview.widget.CardView pantryCard =
                findViewById(R.id.pantryCard);

        pantryCard.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            PantryActivity.class
                    );

            startActivity(intent);
        });
        androidx.cardview.widget.CardView recipesCard =
                findViewById(R.id.recipesCard);

        recipesCard.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
        });

        androidx.cardview.widget.CardView settingsCard =
                findViewById(R.id.settingsCard);

        settingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

        TextView navHome = findViewById(R.id.navHome);
        TextView navPantry = findViewById(R.id.navPantry);
        TextView navRecipes = findViewById(R.id.navRecipes);
        TextView navSettings = findViewById(R.id.navSettings);

        navHome.setOnClickListener(v -> {
            // Already on Home
        });

        navPantry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PantryActivity.class);
            startActivity(intent);
        });

        navRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPantryCount();
        loadExpiringSoonCount();
    }

    private void loadPantryCount() {

        int count = 0;

        Cursor cursor =
                databaseHelper.getAllIngredients();

        if (cursor != null) {

            count = cursor.getCount();

            cursor.close();
        }

        String countText =
                count +
                        (count == 1
                                ? " ingredient"
                                : " ingredients");

        homePantryCount.setText(countText);
    }

    private void loadExpiringSoonCount() {

        int count =
                databaseHelper.getExpiringSoonCount();

        String expiryText =
                count + " expiring soon";

        homeExpiringCount.setText(expiryText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.menu_home) {
            return true;

        } else if (itemId == R.id.menu_pantry) {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            PantryActivity.class
                    )
            );
            return true;

        } else if (itemId == R.id.menu_recipes) {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    )
            );
            return true;

        } else if (itemId == R.id.menu_settings) {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    )
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
