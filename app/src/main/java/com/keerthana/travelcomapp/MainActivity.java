package com.keerthana.travelcomapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ── UI elements ──────────────────────────────────────────────────────────
    private Spinner spinnerCategory;
    private Spinner spinnerSourceUnit;
    private Spinner spinnerDestinationUnit;
    private EditText editTextInputValue;
    private Button buttonConvert;
    private TextView textViewResult;

    // ── Conversion unit lists ─────────────────────────────────────────────────
    private final String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    private final String[] fuelDistanceUnits = {
            "Miles per Gallon (mpg)",
            "Kilometers per Liter (km/L)",
            "Gallons (US)",
            "Liters",
            "Nautical Miles",
            "Kilometers"
    };
    private final String[] temperatureUnits = {
            "Celsius (°C)",
            "Fahrenheit (°F)",
            "Kelvin (K)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link each variable to the corresponding view in the XML layout
        spinnerCategory        = findViewById(R.id.spinnerCategory);
        spinnerSourceUnit      = findViewById(R.id.spinnerSourceUnit);
        spinnerDestinationUnit = findViewById(R.id.spinnerDestinationUnit);
        editTextInputValue     = findViewById(R.id.editTextInputValue);
        buttonConvert          = findViewById(R.id.buttonConvert);
        textViewResult         = findViewById(R.id.textViewResult);

        setupCategorySpinner();

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleConversionRequest();
            }
        });
    }

    // ── Creates an adapter that always shows black text in the spinner ─────────
    // This ensures the selected value is visible against the white background
    private ArrayAdapter<String> createSpinnerAdapter(String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items) {

            // Controls how the selected item looks in the closed spinner
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(14);
                textView.setPadding(8, 8, 8, 8);
                return view;
            }

            // Controls how each item looks in the open dropdown list
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(14);
                textView.setPadding(16, 16, 16, 16);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    // ── Sets up the top-level category dropdown ───────────────────────────────
    private void setupCategorySpinner() {
        String[] categories = {" Currency", " Fuel & Distance", " Temperature"};

        spinnerCategory.setAdapter(createSpinnerAdapter(categories));

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinnersForCategory(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Load currency units by default on first launch
        updateUnitSpinnersForCategory(0);
    }

    // ── Loads the correct From/To unit list based on chosen category ──────────
    private void updateUnitSpinnersForCategory(int categoryIndex) {
        String[] unitsForCategory;

        switch (categoryIndex) {
            case 0:  unitsForCategory = currencyUnits;     break;
            case 1:  unitsForCategory = fuelDistanceUnits; break;
            default: unitsForCategory = temperatureUnits;  break;
        }

        spinnerSourceUnit.setAdapter(createSpinnerAdapter(unitsForCategory));
        spinnerDestinationUnit.setAdapter(createSpinnerAdapter(unitsForCategory));

        // Clear any previous result when the user switches category
        textViewResult.setText("Result will appear here");
    }

    // ── Called when the Convert button is pressed ─────────────────────────────
    private void handleConversionRequest() {
        String rawInput = editTextInputValue.getText().toString().trim();

        // Subtask 4 – Validation: empty input check
        if (rawInput.isEmpty()) {
            Toast.makeText(this, "⚠ Please enter a value to convert.", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(rawInput);
        } catch (NumberFormatException error) {
            // Subtask 4 – Validation: non-numeric input
            Toast.makeText(this, "⚠ Please enter a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedSourceUnit      = spinnerSourceUnit.getSelectedItem().toString();
        String selectedDestinationUnit = spinnerDestinationUnit.getSelectedItem().toString();
        int    selectedCategoryIndex   = spinnerCategory.getSelectedItemPosition();

        // Subtask 4 – Validation: identity conversion (same unit on both sides)
        if (selectedSourceUnit.equals(selectedDestinationUnit)) {
            textViewResult.setText(inputValue + " " + selectedSourceUnit
                    + "\n(No conversion needed — same unit!)");
            return;
        }

        // Subtask 4 – Validation: negative values where they make no physical sense
        if (selectedCategoryIndex == 1 && inputValue < 0) {
            Toast.makeText(this, "⚠ Fuel & distance values cannot be negative.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategoryIndex == 2) {
            // Kelvin cannot go below absolute zero (−273.15 °C)
            if (selectedSourceUnit.contains("Celsius") && inputValue < -273.15) {
                Toast.makeText(this, "⚠ Temperature below absolute zero is impossible.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSourceUnit.contains("Fahrenheit") && inputValue < -459.67) {
                Toast.makeText(this, "⚠ Temperature below absolute zero is impossible.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSourceUnit.contains("Kelvin") && inputValue < 0) {
                Toast.makeText(this, "⚠ Kelvin cannot be negative.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // All validation passed — run the conversion
        double convertedResult = performConversion(
                selectedCategoryIndex,
                selectedSourceUnit,
                selectedDestinationUnit,
                inputValue);

        // Format result: no unnecessary decimals for large numbers like JPY
        String formattedResult = (convertedResult == Math.floor(convertedResult))
                ? String.valueOf((long) convertedResult)
                : String.format("%.4f", convertedResult);

        textViewResult.setText(inputValue + " " + selectedSourceUnit
                + "\n= " + formattedResult + " " + selectedDestinationUnit);
    }

    // ── Core conversion logic (Subtask 2) ─────────────────────────────────────
    // Returns the converted value given a category, source unit, destination
    // unit, and the numeric input provided by the user.
    private double performConversion(int categoryIndex,
                                     String sourceUnit,
                                     String destinationUnit,
                                     double inputValue) {
        switch (categoryIndex) {
            case 0: return convertCurrency(sourceUnit, destinationUnit, inputValue);
            case 1: return convertFuelAndDistance(sourceUnit, destinationUnit, inputValue);
            case 2: return convertTemperature(sourceUnit, destinationUnit, inputValue);
        }
        return inputValue; // Fallback — should never be reached
    }

    // ── Currency conversion ───────────────────────────────────────────────────
    // Strategy: convert any currency → USD first, then USD → target currency.
    // Fixed 2026 rates as specified in the task sheet.
    private double convertCurrency(String sourceUnit,
                                   String destinationUnit,
                                   double inputValue) {
        double amountInUsd = toUsd(sourceUnit, inputValue);
        return fromUsd(destinationUnit, amountInUsd);
    }

    // Converts a given amount in the specified currency into its USD equivalent
    private double toUsd(String currencyCode, double amount) {
        switch (currencyCode) {
            case "USD": return amount;
            case "AUD": return amount / 1.55;
            case "EUR": return amount / 0.92;
            case "JPY": return amount / 148.50;
            case "GBP": return amount / 0.78;
        }
        return amount;
    }

    // Converts a USD amount into the specified target currency
    private double fromUsd(String currencyCode, double amountInUsd) {
        switch (currencyCode) {
            case "USD": return amountInUsd;
            case "AUD": return amountInUsd * 1.55;
            case "EUR": return amountInUsd * 0.92;
            case "JPY": return amountInUsd * 148.50;
            case "GBP": return amountInUsd * 0.78;
        }
        return amountInUsd;
    }

    // ── Fuel & Distance conversion ────────────────────────────────────────────
    private double convertFuelAndDistance(String sourceUnit,
                                          String destinationUnit,
                                          double inputValue) {
        // MPG ↔ km/L
        if (sourceUnit.contains("mpg") && destinationUnit.contains("km/L"))
            return inputValue * 0.425;
        if (sourceUnit.contains("km/L") && destinationUnit.contains("mpg"))
            return inputValue / 0.425;

        // Gallons ↔ Litres
        if (sourceUnit.contains("Gallons") && destinationUnit.contains("Liters"))
            return inputValue * 3.785;
        if (sourceUnit.contains("Liters") && destinationUnit.contains("Gallons"))
            return inputValue / 3.785;

        // Nautical Miles ↔ Kilometres
        if (sourceUnit.contains("Nautical") && destinationUnit.contains("Kilometers"))
            return inputValue * 1.852;
        if (sourceUnit.contains("Kilometers") && destinationUnit.contains("Nautical"))
            return inputValue / 1.852;

        return inputValue;
    }

    // ── Temperature conversion ────────────────────────────────────────────────
    private double convertTemperature(String sourceUnit,
                                      String destinationUnit,
                                      double inputValue) {
        // Celsius → Fahrenheit
        if (sourceUnit.contains("Celsius") && destinationUnit.contains("Fahrenheit"))
            return (inputValue * 1.8) + 32;

        // Fahrenheit → Celsius
        if (sourceUnit.contains("Fahrenheit") && destinationUnit.contains("Celsius"))
            return (inputValue - 32) / 1.8;

        // Celsius → Kelvin
        if (sourceUnit.contains("Celsius") && destinationUnit.contains("Kelvin"))
            return inputValue + 273.15;

        // Kelvin → Celsius
        if (sourceUnit.contains("Kelvin") && destinationUnit.contains("Celsius"))
            return inputValue - 273.15;

        // Fahrenheit → Kelvin (via Celsius)
        if (sourceUnit.contains("Fahrenheit") && destinationUnit.contains("Kelvin"))
            return ((inputValue - 32) / 1.8) + 273.15;

        // Kelvin → Fahrenheit (via Celsius)
        if (sourceUnit.contains("Kelvin") && destinationUnit.contains("Fahrenheit"))
            return ((inputValue - 273.15) * 1.8) + 32;

        return inputValue;
    }
}