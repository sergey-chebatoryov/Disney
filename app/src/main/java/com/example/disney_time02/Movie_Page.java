package com.example.disney_time02;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class Movie_Page extends AppCompatActivity {
    RecyclerView recyclerView;
    Vector<youTubeVideos> youtubeVideos = new Vector<>();
    private String movieId;
    private String movieName;
    boolean switchChecked;
    AlertDialog dialog;
    Runnable runnableSave = () -> {
        Looper.prepare();
        saveMovie();
    };
    Runnable runnableCheck = () -> {
        Looper.prepare();
        checkMovie();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);

        //Set text box Genre
        String genre = getIntent().getExtras().getString("genre", "");
        EditText edGenre = findViewById(R.id.genres);
        edGenre.setText(genre);

        //Set text box Movie name
        movieName = getIntent().getExtras().getString("movieName", "");
        EditText edMovieName = findViewById(R.id.editTextId);
        edMovieName.setText(movieName);

        prepareMovieView();

        prepareSwitchView();

    }

    private void prepareMovieView() {
        String movieUrl = getIntent().getExtras().getString("url", "");
        movieId = getIntent().getExtras().getString("movieId", "");

        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" +
                getId(movieUrl) + "\" frameborder=\"0\" allowfullscreen></iframe>";

        recyclerView = findViewById(R.id.trailerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        youtubeVideos.add(new youTubeVideos(url));
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);
        recyclerView.requestFocus();
    }

    private void prepareSwitchView() {
        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .create();
        dialog.show();
        new Thread(runnableCheck).start();
    }

    private void checkMovie() {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String result = mysqlConnect.selectColumn("select name from usersmovie where name='" + LoginActivity.userName + "' and id=" + movieId, "name");
        SwitchCompat switchCompat = findViewById(R.id.save);
        switchCompat.setChecked(result != null);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> save(isChecked));
        dialog.dismiss();
    }

    public void save(boolean isChecked) {
        this.switchChecked = isChecked;
        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .create();
        dialog.show();
        new Thread(runnableSave).start();
    }

    private String getId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void saveMovie() {
        String sql;
        if (switchChecked) {
            sql = "insert into usersmovie (name, id) values ('" + LoginActivity.userName + "', " + movieId + ")";
        } else {
            sql = " delete from usersmovie where name='" + LoginActivity.userName + "' and id=" + movieId;
        }
        MysqlConnect mysqlConnect = new MysqlConnect();
        int result = mysqlConnect.executeSql(sql);
        if (result > 0) {
            Toast.makeText(this, "Movie " + movieName + " added", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    public void goBack(View view) {
        finish();
    }
}
