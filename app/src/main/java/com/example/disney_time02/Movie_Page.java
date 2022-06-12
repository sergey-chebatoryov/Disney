package com.example.disney_time02;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class Movie_Page extends AppCompatActivity {
    private EditText edMovieName, edGenre;
    RecyclerView recyclerView;
    Vector<youTubeVideos> youtubeVideos = new Vector<>();
    private String movieId;
    private String movieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove the application title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie_page);

        String genre = getIntent().getExtras().getString("genre", "");
        movieName = getIntent().getExtras().getString("movieName", "");
        String movieUrl = getIntent().getExtras().getString("url", "");
        movieId = getIntent().getExtras().getString("movieId", "");
        edMovieName = findViewById(R.id.editTextId);
        edMovieName.setText(movieName);
        edGenre = findViewById(R.id.genres);
        edGenre.setText(genre);

        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" +
                getId(movieUrl) + "\" frameborder=\"0\" allowfullscreen></iframe>";

        recyclerView = findViewById(R.id.trailerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        youtubeVideos.add(new youTubeVideos(url));
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);
    }

    private String getId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }


    public void goBack(View view) {
        finish();
    }

    public void save(View view) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        mysqlConnect.insert("insert into usersmovie (name, id) values ('"
                + LoginActivity.userName + "', '" + movieId + "')");
        if (mysqlConnect.getResultInsert() > 0) {
            Toast.makeText(this, "Movie " + movieName + " added", Toast.LENGTH_SHORT).show();
        }
    }
}