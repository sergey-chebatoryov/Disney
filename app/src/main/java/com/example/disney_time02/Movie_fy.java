package com.example.disney_time02;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class Movie_fy extends AppCompatActivity {
    RecyclerView recyclerView;
    Vector<youTubeVideos> youtubeVideos = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_fy);
        selectMovie();
    }

    private void selectMovie() {
        String genre = getIntent().getExtras().getString("genre", "");
        MysqlConnect mysqlConnect = new MysqlConnect();
        mysqlConnect.start("select * from movies where genre='" + genre + "'");
        Map<Integer, Map<String, String>> moviesMap = mysqlConnect.getResult();
        Collection<Map<String, String>> rows = moviesMap.values();
        for (Map<String, String> row : rows) {
            Toast.makeText(this, row.get("name") + " " + row.get("trailer"), Toast.LENGTH_SHORT).show();
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (Map<String, String> row : rows) {
            String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" +
                    getId(Objects.requireNonNull(row.get("trailer"))) + "\" frameborder=\"0\" allowfullscreen></iframe>";
            youtubeVideos.add(new youTubeVideos(url));
        }
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);

    }

    private String getId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public void goBack(View view) {
        finish();
    }
}