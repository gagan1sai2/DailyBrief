package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private double latitude, longitude;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Get location and category from Intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        category = getIntent().getStringExtra("category");

        if (latitude == 0.0 || longitude == 0.0 || category == null) {
            Toast.makeText(this, "Missing location or category", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ✅ Open Google Maps with search query near the location
        openGoogleMaps(category, latitude, longitude);
    }

    private void openGoogleMaps(String category, double lat, double lon) {
        try {
            // Create the URI — searches for “category near given coordinates”
            String uri = "geo:" + lat + "," + lon + "?q=" + Uri.encode(category + " near me");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            // ✅ Start the intent safely
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Google Maps app not found", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error opening map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Finish activity after launching Maps
        finish();
    }
}
