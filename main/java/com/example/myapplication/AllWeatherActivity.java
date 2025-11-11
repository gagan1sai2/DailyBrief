package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AllWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_weather);

        ArrayList<WeatherItem> weatherList = getIntent().getParcelableArrayListExtra("weatherList");

        TextView city = findViewById(R.id.detailCity);
        TextView temp = findViewById(R.id.detailTemp);
        TextView condition = findViewById(R.id.detailCondition);
        TextView humidity = findViewById(R.id.detailHumidity);
        TextView wind = findViewById(R.id.detailWind);
        TextView pressure = findViewById(R.id.detailPressure);
        TextView cloudiness = findViewById(R.id.detailCloudiness);

        if (weatherList != null && !weatherList.isEmpty()) {
            WeatherItem item = weatherList.get(0);

            // ✅ Set actual values dynamically
            city.setText(item.getCity()); // <-- this will display the real city you selected
            temp.setText(item.getTemperature());
            condition.setText(item.getCondition());
            humidity.setText("Humidity: " + item.getHumidity());
            wind.setText("Wind: " + item.getWindSpeed());
            pressure.setText("Pressure: " + item.getPressure());
            cloudiness.setText("Cloudiness: " + item.getClouds());
        }
    }
}
