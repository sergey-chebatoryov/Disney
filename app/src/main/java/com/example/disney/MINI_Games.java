package com.example.disney;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MINI_Games extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_games);
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

    public void goBack(View view) {
        finish();
    }

    public void goPlay1(View view) {
        Intent intent = new Intent(this, R_princess.class);
        startActivity(intent);
    }

    public void goPlay2(View view) {
        Intent intent = new Intent(this, R_character.class);
        startActivity(intent);
    }

    public void goPlay3(View view) {
        Intent intent = new Intent(this, R_role.class);
        startActivity(intent);
    }

    public void goPlay4(View view) {
        Intent intent = new Intent(this, R_food.class);
        startActivity(intent);
    }
}