package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // 🌦 Weather info variables
    private String tempValue = "--";
    private String conditionValue = "--";
    private String humidityValue = "--";
    private String windValue = "--";
    private String pressureValue = "--";
    private String cloudsValue = "--";

    public static final String WEATHER_API_KEY = "f4438e2683c295eabe036ca9ae1807f6";
    public static final String NEWS_API_KEY = "159ac75a773913248fd14863d798179c";

    private OkHttpClient client;
    private TextView weatherSubtitle, newsSubtitle, greetingText, greetingSubtitle;
    private String currentCity, username;
    private SharedPreferences prefs;
    private RelativeLayout mainLayout;

    private ArrayList<NewsItem> newsList = new ArrayList<>();
    private ArrayList<WeatherItem> weatherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        client = new OkHttpClient();

        prefs = getSharedPreferences("DailyBriefPrefs", MODE_PRIVATE);
        currentCity = prefs.getString("city", "Delhi,IN");
        username = prefs.getString("username", "User");

        mainLayout = findViewById(R.id.mainLayout);
        weatherSubtitle = findViewById(R.id.weatherSubtitle);
        newsSubtitle = findViewById(R.id.newsSubtitle);
        greetingText = findViewById(R.id.greetingText);
        greetingSubtitle = findViewById(R.id.greetingSubtitle);

        // 📍 Change location button
        ImageView changeLocationBtn = findViewById(R.id.changeLocationBtn);
        changeLocationBtn.setOnClickListener(v -> showChangeLocationDialog());

        // 🅰 Font toggle button
        ImageView changeFontBtn = findViewById(R.id.changeFontBtn);
        changeFontBtn.setOnClickListener(v -> toggleAppFont());

        // 🌦 Weather & News cards
        CardView weatherCard = findViewById(R.id.weatherCard);
        CardView newsCard = findViewById(R.id.newsCard);
        weatherCard.setOnClickListener(v -> openDetailedWeather());
        newsCard.setOnClickListener(v -> openDetailedNews());

        // 🔍 Quick search buttons
        CardView searchStationsBtn = findViewById(R.id.searchStationsBtn);
        CardView searchRestaurantsBtn = findViewById(R.id.searchRestaurantsBtn);
        CardView searchAtmsBtn = findViewById(R.id.searchAtmsBtn);

        searchStationsBtn.setOnClickListener(v -> showStationOptions());
        searchRestaurantsBtn.setOnClickListener(v -> showRestaurantOptions());
        searchAtmsBtn.setOnClickListener(v -> showAtmOptions());

        updateGreeting();
        loadData();

        // 🧠 Apply saved font preference
        boolean isCursive = prefs.getBoolean("isCursiveFont", false);
        applyFontToAllViews(isCursive);
    }

    // -------------------- FONT CHANGE LOGIC -------------------- //
    private void toggleAppFont() {
        boolean isCursive = prefs.getBoolean("isCursiveFont", false);
        boolean newState = !isCursive;

        prefs.edit().putBoolean("isCursiveFont", newState).apply();
        applyFontToAllViews(newState);

        Toast.makeText(this,
                newState ? "Cursive font applied ✨" : "Default font restored",
                Toast.LENGTH_SHORT).show();
    }

    private void applyFontToAllViews(boolean useCursive) {
        int fontRes = useCursive ? R.font.dancing_script : R.font.roboto_regular;
        View rootView = findViewById(android.R.id.content);
        applyFontRecursive(rootView, fontRes);
    }

    private void applyFontRecursive(View view, int fontRes) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(getResources().getFont(fontRes));
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyFontRecursive(group.getChildAt(i), fontRes);
            }
        }
    }

    // -------------------- CUSTOM DIALOG -------------------- //
    private void showCustomOptionDialog(String title, String[] options, DialogInterface.OnClickListener listener) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_options, null);
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        ListView dialogList = dialogView.findViewById(R.id.dialogList);

        dialogTitle.setText(title);
        dialogTitle.setTextColor(Color.WHITE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                text.setTextSize(17f);
                return view;
            }
        };
        dialogList.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        dialogList.setOnItemClickListener((parent, view, position, id) -> {
            listener.onClick(dialog, position);
            dialog.dismiss();
        });

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }

    // -------------------- STATION OPTIONS -------------------- //
    private void showStationOptions() {
        String[] options = {"Bus Stations", "Railway Stations"};
        showCustomOptionDialog("Select Station Type", options, (dialog, which) -> {
            String query = (which == 0)
                    ? "bus stations in " + currentCity
                    : "railway stations in " + currentCity;
            openInGoogleMaps(query);
        });
    }

    // -------------------- RESTAURANT OPTIONS -------------------- //
    private void showRestaurantOptions() {
        String[] options = {"Veg Restaurants", "Non-Veg Restaurants"};
        showCustomOptionDialog("Select Restaurant Type", options, (dialog, which) -> {
            String query = (which == 0)
                    ? "veg restaurants in " + currentCity
                    : "non veg restaurants in " + currentCity;
            openInGoogleMaps(query);
        });
    }

    // -------------------- ATM OPTIONS -------------------- //
    private void showAtmOptions() {
        String[] options = {"Nearby ATMs", "24x7 ATMs", "Cash Deposit Machines"};
        showCustomOptionDialog("Select ATM Type", options, (dialog, which) -> {
            String query;
            if (which == 0) query = "ATMs in " + currentCity;
            else if (which == 1) query = "24x7 ATMs in " + currentCity;
            else query = "cash deposit machines in " + currentCity;
            openInGoogleMaps(query);
        });
    }

    // -------------------- OPEN MAPS -------------------- //
    private void openInGoogleMaps(String query) {
        try {
            if (query == null || query.isEmpty()) {
                Toast.makeText(this, "Missing location or category", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = "https://www.google.com/maps/search/?api=1&query=" + query.replace(" ", "+");
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

    // -------------------- GREETING -------------------- //
    private void updateGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12) greeting = "Good Morning";
        else if (hour < 17) greeting = "Good Afternoon";
        else if (hour < 21) greeting = "Good Evening";
        else greeting = "Good Night";

        greetingText.setText(greeting + ", " + username + " 👋");
        greetingSubtitle.setText("Here’s your daily brief for today.");
    }

    // -------------------- LOAD WEATHER & NEWS -------------------- //
    private void loadData() {
        showWeather();
        showNews();
    }

    // -------------------- CHANGE LOCATION -------------------- //
    private void showChangeLocationDialog() {
        AutoCompleteTextView input = new AutoCompleteTextView(this);
        input.setHint("Enter city name (e.g., Vellore,IN)");
        input.setThreshold(1);

// Disable keyboard suggestions & autocorrect
        input.setInputType(android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

// ✅ Restrict to only your list of known cities
        String[] cities = {
                "Vellore,IN", "Delhi,IN", "Paris,FR", "New York,US",
                "Tokyo,JP", "Sydney,AU", "London,UK", "Toronto,CA", "Singapore,SG"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
        input.setAdapter(adapter);

        new AlertDialog.Builder(this)
                .setTitle("Change Location")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newCity = input.getText().toString().trim();
                    if (!newCity.isEmpty()) {
                        currentCity = newCity;
                        prefs.edit().putString("city", newCity).apply();
                        loadData();

                        // 🌍 Toast confirmation message
                        Toast.makeText(this, "Location changed to " + newCity + " 🌆", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please enter a valid city name ⚠️", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // -------------------- WEATHER -------------------- //
    private void showWeather() {
        if (currentCity == null || currentCity.isEmpty()) {
            runOnUiThread(() -> weatherSubtitle.setText("No city selected"));
            return;
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + currentCity + "&appid=" + WEATHER_API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0")
                .build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> weatherSubtitle.setText("Weather unavailable"));
                    return;
                }

                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject main = jsonObject.optJSONObject("main");
                JSONObject wind = jsonObject.optJSONObject("wind");
                JSONObject clouds = jsonObject.optJSONObject("clouds");
                JSONArray weatherArray = jsonObject.optJSONArray("weather");

                tempValue = main != null ? main.optString("temp", "--") + "°C" : "--";
                conditionValue = (weatherArray != null && weatherArray.length() > 0)
                        ? weatherArray.getJSONObject(0).optString("description", "--") : "--";
                humidityValue = main != null ? main.optString("humidity", "--") + "%" : "--";
                pressureValue = main != null ? main.optString("pressure", "--") + " hPa" : "--";
                windValue = wind != null ? wind.optString("speed", "--") + " m/s" : "--";
                cloudsValue = clouds != null ? clouds.optString("all", "--") + "%" : "--";

                weatherList.clear();
                weatherList.add(new WeatherItem(
                        currentCity, tempValue, conditionValue,
                        humidityValue, windValue, pressureValue, cloudsValue
                ));

                runOnUiThread(() -> weatherSubtitle.setText(currentCity + ": " + tempValue + " | " + conditionValue));
            } catch (Exception e) {
                runOnUiThread(() -> weatherSubtitle.setText("Error fetching weather"));
            }
        }).start();
    }

    // -------------------- NEWS -------------------- //
    private void showNews() {
        String url = "https://gnews.io/api/v4/search?q=" + currentCity + "&lang=en&country=us&max=10&apikey=" + NEWS_API_KEY;
        Request request = new Request.Builder().url(url).header("User-Agent", "Mozilla/5.0").build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> newsSubtitle.setText("News unavailable"));
                    return;
                }

                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray articles = jsonObject.optJSONArray("articles");

                if (articles == null || articles.length() == 0) {
                    runOnUiThread(() -> newsSubtitle.setText("No news found"));
                    return;
                }

                newsList.clear();
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    newsList.add(new NewsItem(
                            article.optString("title", ""),
                            article.optString("description", ""),
                            article.optString("url", "")
                    ));
                }

                runOnUiThread(() -> newsSubtitle.setText(newsList.get(0).title));
            } catch (Exception e) {
                runOnUiThread(() -> newsSubtitle.setText("Error loading news"));
            }
        }).start();
    }

    // -------------------- OPEN DETAIL SCREENS -------------------- //
    // -------------------- OPEN DETAIL SCREENS -------------------- //
    private void openDetailedNews() {
        if (newsList == null || newsList.isEmpty()) {
            Toast.makeText(this, "No news available for this location 📰", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, AllNewsActivity.class);
        intent.putParcelableArrayListExtra("newsList", newsList);
        startActivity(intent);
    }


    private void openDetailedWeather() {
        if (weatherList == null || weatherList.isEmpty()) {
            Toast.makeText(this, "Weather data not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, AllWeatherActivity.class);
        intent.putParcelableArrayListExtra("weatherList", weatherList);
        startActivity(intent);
    }
}
