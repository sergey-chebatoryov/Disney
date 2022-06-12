package com.example.disney_time02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Movie_fy extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    Map<Integer, Map<String, String>> moviesMap;
    String genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_fy);
        selectMovie();
    }

    private void selectMovie() {
        genre = getIntent().getExtras().getString("genre", "");
        MysqlConnect mysqlConnect = new MysqlConnect();
        mysqlConnect.select("select * from movies where genre='" + genre + "'");
        moviesMap = mysqlConnect.getResultSelect();
        Collection<Map<String, String>> rows = moviesMap.values();
        ArrayList<String> rowsString = new ArrayList<>();
        for (Map<String, String> row : rows) {
            rowsString.add(row.get("name") + " " + row.get("trailer"));
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, rowsString);
        adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Movie_Page.class).putExtra("genre", genre);
        Map<String, String> row = moviesMap.get(position);
        String movieName = Objects.requireNonNull(row).get("name");
        String url = Objects.requireNonNull(row).get("trailer");
        intent.putExtra("movieName", movieName);
        intent.putExtra("url", url);
        startActivity(intent);

    }

    public void goBack(View view) {
        finish();
    }
}