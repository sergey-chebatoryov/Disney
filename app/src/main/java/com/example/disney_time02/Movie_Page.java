package com.example.disney_time02;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    AlertDialog dialog;
    Runnable runnable = () -> {
        Looper.prepare();
        saveMovie();
    };

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,

        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
        dialog.show();

        new Thread(runnable).start();
    }

    private void saveMovie() {
        MysqlConnect mysqlConnect = new MysqlConnect();
        int insert = mysqlConnect.insert("insert into usersmovie (name, id) values ('"
                + LoginActivity.userName + "', '" + movieId + "')");
        if (insert > 0) {
            Toast.makeText(this, "Movie " + movieName + " added", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
