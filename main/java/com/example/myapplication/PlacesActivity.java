package com.example.myapplication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlacesActivity extends AppCompatActivity {

    private double latitude = 12.9716; // Default → Bengaluru (for testing)
    private double longitude = 77.5946; // You can replace this with live location later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Button btnBus = findViewById(R.id.btnBus);
        Button btnTrain = findViewById(R.id.btnTrain);
        Button btnAtm = findViewById(R.id.btnAtm);
        Button btnRestaurant = findViewById(R.id.btnRestaurant);

        btnBus.setOnClickListener(v -> openGoogleMaps("bus stations"));
        btnTrain.setOnClickListener(v -> openGoogleMaps("railway stations"));
        btnAtm.setOnClickListener(v -> openGoogleMaps("ATMs"));
        btnRestaurant.setOnClickListener(v -> openGoogleMaps("restaurants"));
    }

    private void openGoogleMaps(String query) {
        try {
            // ✅ Build search URL for Google Maps
            String geoUri = "geo:" + latitude + "," + longitude + "?q=" + query;
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(geoUri));
            intent.setPackage("com.google.android.apps.maps"); // Open only in Google Maps
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open Maps", Toast.LENGTH_SHORT).show();
        }
    }
}
