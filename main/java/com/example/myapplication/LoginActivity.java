package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("DailyBriefPrefs", MODE_PRIVATE);

        // Check if username is already saved — skip login if yes
        if (prefs.contains("username")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        EditText usernameInput = findViewById(R.id.usernameInput);
        Button startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putString("username", username).apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
