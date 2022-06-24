package com.example.disney_time02;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Movie_Search extends AppCompatActivity {
    private RadioGroup rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
        setActionBar("");
        this.rd = findViewById(R.id.rd);
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

    public void goBack(View view) {
        finish();
    }

    public void goShow(View view) {
        int checkedRadioButtonId = rd.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);
        String genre = radioButton.getText().toString();
        Intent intent = new Intent(this, Movie_fy.class).putExtra("genre", genre);
        startActivity(intent);
    }
}