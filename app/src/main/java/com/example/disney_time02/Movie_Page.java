package com.example.disney_time02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class Movie_Page extends AppCompatActivity {
    Activity mainActivity = this; // If you are in activity
    WebView videoWeb;
    private String movieId;
    private String movieName;
    boolean switchChecked;
    SwitchCompat switchCompat;
    AlertDialog dialog;
    Runnable runnableSave = () -> {
        Looper.prepare();
        saveMovie();
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

        //Save movie ID
        movieId = getIntent().getExtras().getString("movieId", "");
        switchCompat = findViewById(R.id.save);

        //Get movie saved in database
        String savedMovie = getIntent().getExtras().getString("savedMovie");
        switchCompat.setChecked(savedMovie != null);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> save(isChecked));

        //Create spinner dialog
        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .create();

        //prepare video player
        getVideoView();
        prepareMovieView();
    }

    private void prepareMovieView() {
        String movieUrl = getIntent().getExtras().getString("url", "");
        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" +
                getId(movieUrl) + "\" frameborder=\"0\" allowfullscreen></iframe>";
        videoWeb.loadData( url, "text/html" , "utf-8");
    }

    public void save(boolean isChecked) {
        this.switchChecked = isChecked;
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
            sql = "delete from usersmovie where name='" + LoginActivity.userName + "' and id=" + movieId;
        }
        MysqlConnect mysqlConnect = new MysqlConnect();
        if (mysqlConnect.executeSql(sql, this) > 0) {
            Toast.makeText(this,
                    "Movie " + movieName + (switchChecked ? " added to" : " removed from") + " your favorites",
                    Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    public void goBack(View view) {
        finish();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void getVideoView(){
        videoWeb = findViewById(R.id.trailerView);
        videoWeb.getSettings().setJavaScriptEnabled(true);
        videoWeb.setWebChromeClient(new WebChromeClient() {
            private View mCustomView;
            private WebChromeClient.CustomViewCallback mCustomViewCallback;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;

            public Bitmap getDefaultVideoPoster() {
                if (mainActivity == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), 2130837573);
            }

            public void onHideCustomView() {
                ((FrameLayout) mainActivity.getWindow().getDecorView()).removeView(this.mCustomView);
                this.mCustomView = null;
                mainActivity.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                mainActivity.setRequestedOrientation(this.mOriginalOrientation);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
                if (this.mCustomView != null) {
                    onHideCustomView();
                    return;
                }
                this.mCustomView = paramView;
                this.mOriginalSystemUiVisibility = mainActivity.getWindow().getDecorView().getSystemUiVisibility();
                this.mOriginalOrientation = mainActivity.getRequestedOrientation();
                this.mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout) mainActivity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                mainActivity.getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        });
    }

}
