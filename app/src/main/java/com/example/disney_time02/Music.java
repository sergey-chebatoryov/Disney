package com.example.disney_time02;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Music extends AppCompatActivity {
    private Button play, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        this.play = findViewById(R.id.play);
        this.stop = findViewById(R.id.stop);
        setActionBar("");
    }

    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(androidx.cardview.R.color.cardview_dark_background, null)));
        actionBar.setTitle(heading);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        MenuClass menuClass = new MenuClass(this);
        return menuClass.getItemSelected(item);
    }

    public void play(View view) {
        startService(new Intent(Music.this, MyService.class));
    }

    public void stop(View view) {
        stopService(new Intent(Music.this, MyService.class));
    }

    public void goBack(View view) {
        finish();
    }
}