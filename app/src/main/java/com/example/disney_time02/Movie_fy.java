package com.example.disney_time02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
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
    ArrayList<String> rowsString;
    AlertDialog dialog;
    Runnable runnable = this::selectMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_fy);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rowsString = new ArrayList<>();
        adapter = new MyRecyclerViewAdapter(this, rowsString);
        adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
        dialog.show();

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void selectMovie() {
        String genre = getIntent().getExtras().getString("genre", "");
        MysqlConnect mysqlConnect = new MysqlConnect();
        moviesMap = mysqlConnect.select("select * from movies where genre='" + genre + "'");
        Collection<Map<String, String>> rows = moviesMap.values();
        for (Map<String, String> row : rows) {
            rowsString.add(row.get("name") + " " + row.get("trailer"));
        }
        adapter.notifyItemRangeInserted(0, rowsString.size());
        dialog.dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Movie_Page.class);

        Map<String, String> row = moviesMap.get(position);
        String genre = Objects.requireNonNull(row).get("genre");
        String movieName = Objects.requireNonNull(row).get("name");
        String url = Objects.requireNonNull(row).get("trailer");
        String movieId = Objects.requireNonNull(row).get("id");

        intent.putExtra("genre", genre)
                .putExtra("movieName", movieName)
                .putExtra("url", url)
                .putExtra("movieId", movieId);
        startActivity(intent);
    }

    public void goBack(View view) {
        finish();
    }
}