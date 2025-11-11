package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AllNewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<NewsItem> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);

        recyclerView = findViewById(R.id.recyclerViewAllNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsList = getIntent().getParcelableArrayListExtra("newsList");
        if (newsList == null) newsList = new ArrayList<>();

        if (newsList.isEmpty()) {
            // Create a centered "No news available" message
            TextView noNewsText = new TextView(this);
            noNewsText.setText("No news available for this location 🗞️");
            noNewsText.setTextSize(18);
            noNewsText.setGravity(Gravity.CENTER);
            noNewsText.setTextColor(getResources().getColor(android.R.color.black));

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.addView(noNewsText);

            setContentView(layout);
            return;
        }

        // If there are one or more news, show them scrollable
        adapter = new NewsAdapter(newsList, item -> {
            WebViewActivity.open(this, item.url);
        });

        recyclerView.setAdapter(adapter);
    }
}
