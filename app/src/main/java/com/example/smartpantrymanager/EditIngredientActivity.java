package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditIngredientActivity extends AppCompatActivity {

    private EditText etEditIngredientName;
    private EditText etEditQuantity;
    private EditText etEditExpiryDate;
    private Spinner spinnerEditUnit;

    private DatabaseHelper databaseHelper;

    private int ingredientId;

    private final String[] units = {
            "pieces",
            "kg",
            "g",
            "litres",
            "ml",
            "packets"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        etEditIngredientName =
                findViewById(R.id.etEditIngredientName);

        etEditQuantity =
                findViewById(R.id.etEditQuantity);

        etEditExpiryDate =
                findViewById(R.id.etEditExpiryDate);

        spinnerEditUnit =
                findViewById(R.id.spinnerEditUnit);

        TextView btnBackEdit =
                findViewById(R.id.btnBackEdit);

        Button btnUpdateIngredient =
                findViewById(R.id.btnUpdateIngredient);

        Button btnDeleteIngredient =
                findViewById(R.id.btnDeleteIngredient);

        databaseHelper = new DatabaseHelper(this);

        // Get the selected ingredient ID
        ingredientId = getIntent().getIntExtra(
                "ingredient_id",
                -1
        );

        // Set up unit spinner
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerEditUnit.setAdapter(adapter);

        // Load ingredient information
        loadIngredientData();

        // Back button
        btnBackEdit.setOnClickListener(v -> finish());

        // Expiry date picker
        etEditExpiryDate.setOnClickListener(v -> showDatePicker());

// Allow the user to clear the optional expiry date
        etEditExpiryDate.setOnLongClickListener(v -> {
            etEditExpiryDate.setText("");
            Toast.makeText(
                    this,
                    "Expiry date cleared",
                    Toast.LENGTH_SHORT
            ).show();
            return true;
        });

        // Update
        btnUpdateIngredient.setOnClickListener(
                v -> updateIngredient()
        );

        // Delete
        btnDeleteIngredient.setOnClickListener(
                v -> deleteIngredient()
        );
    }

    private void loadIngredientData() {

        if (ingredientId == -1) {

            Toast.makeText(
                    this,
                    "Ingredient could not be found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        android.database.Cursor cursor =
                databaseHelper.getAllIngredients();

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

                if (cursor.getInt(idIndex) == ingredientId) {

                    etEditIngredientName.setText(
                            cursor.getString(nameIndex)
                    );

                    etEditQuantity.setText(
                            String.valueOf(
                                    cursor.getDouble(quantityIndex)
                            )
                    );

                    etEditExpiryDate.setText(
                            cursor.getString(expiryIndex)
                    );

                    String savedUnit =
                            cursor.getString(unitIndex);

                    for (int i = 0; i < units.length; i++) {

                        if (units[i].equals(savedUnit)) {

                            spinnerEditUnit.setSelection(i);
                            break;
                        }
                    }

                    break;
                }
            }

            cursor.close();
        }
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear,
                         selectedMonth, selectedDay) -> {

                            String date = String.format(
                                    "%02d/%02d/%04d",
                                    selectedDay,
                                    selectedMonth + 1,
                                    selectedYear
                            );

                            etEditExpiryDate.setText(date);
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    private void updateIngredient() {

        String name =
                etEditIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                etEditQuantity
                        .getText()
                        .toString()
                        .trim();

        String expiryDate =
                etEditExpiryDate
                        .getText()
                        .toString()
                        .trim();

        String unit =
                spinnerEditUnit
                        .getSelectedItem()
                        .toString();

        // Validate name
        if (TextUtils.isEmpty(name)) {

            etEditIngredientName.setError(
                    "Enter an ingredient name"
            );

            etEditIngredientName.requestFocus();
            return;
        }

        // Validate quantity
        if (TextUtils.isEmpty(quantityText)) {

            etEditQuantity.setError(
                    "Enter a quantity"
            );

            etEditQuantity.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            etEditQuantity.setError(
                    "Enter a valid number"
            );

            etEditQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {

            etEditQuantity.setError(
                    "Quantity must be greater than 0"
            );

            etEditQuantity.requestFocus();
            return;
        }

        // Expiry date is optional

        // Update database
        int result =
                databaseHelper.updateIngredient(
                        ingredientId,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

        if (result > 0) {

            Toast.makeText(
                    this,
                    "✓ Ingredient updated!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not update ingredient.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void deleteIngredient() {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete ingredient?")
                .setMessage("Are you sure you want to remove this ingredient from your pantry?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {

                    int result =
                            databaseHelper.deleteIngredient(
                                    ingredientId
                            );

                    if (result > 0) {

                        Toast.makeText(
                                this,
                                "✓ Ingredient deleted!",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                this,
                                "Could not delete ingredient.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
