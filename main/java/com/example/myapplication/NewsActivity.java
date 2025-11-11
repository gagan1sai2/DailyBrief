package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<NewsItem> newsList;
    private TextView noNewsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);

        recyclerView = findViewById(R.id.recyclerViewAllNews);
        noNewsText = findViewById(R.id.noNewsText);  // 👈 make sure to add this TextView in XML

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Get news list from intent
        newsList = getIntent().getParcelableArrayListExtra("newsList");
        if (newsList == null) newsList = new ArrayList<>();

        // ✅ Check if empty
        if (newsList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noNewsText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noNewsText.setVisibility(View.GONE);

            adapter = new NewsAdapter(newsList, item ->
                    WebViewActivity.open(NewsActivity.this, item.url)
            );
            recyclerView.setAdapter(adapter);
        }
    }
}
