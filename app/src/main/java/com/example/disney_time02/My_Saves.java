package com.example.disney_time02;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class My_Saves extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    Map<Integer, Map<String, String>> moviesMap;
    ArrayList<String> rowsString;
    AlertDialog dialog;
    private Integer position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saves);

        recyclerView = findViewById(R.id.savedMovieView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rowsString = new ArrayList<>();
        adapter = new MyRecyclerViewAdapter(this, rowsString);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .setTitle("Searching movies...").create();
        dialog.show();

        new Thread(this::selectMovie).start();
    }

    private void selectMovie() {
        MysqlConnect mysqlConnect = new MysqlConnect();
        moviesMap = mysqlConnect.select("SELECT b.id, a.name, a.genre, trailer FROM movies a JOIN usersmovie b ON b.id=a.id where b.name='" + LoginActivity.userName + "'", this);
        Collection<Map<String, String>> rows = moviesMap.values();
        for (Map<String, String> row : rows) {
            rowsString.add(row.get("name") + " " + row.get("trailer"));
        }
        adapter.notifyItemRangeInserted(0, rowsString.size());
        dialog.dismiss();
    }

    private void checkMovie() {
        Map<String, String> row = moviesMap.get(position);
        String genre = Objects.requireNonNull(row).get("genre");
        String movieName = Objects.requireNonNull(row).get("name");
        String url = Objects.requireNonNull(row).get("trailer");
        String movieId = Objects.requireNonNull(row).get("id");
        MysqlConnect mysqlConnect = new MysqlConnect();

        String result = mysqlConnect.selectColumn("select name from usersmovie where name='" +
                LoginActivity.userName + "' and id=" + movieId, "name", this);

        Intent intent = new Intent(this, Movie_Page.class);
        intent.putExtra("genre", genre)
                .putExtra("movieName", movieName)
                .putExtra("url", url)
                .putExtra("movieId", movieId)
                .putExtra("savedMovie", result);
        startActivity(intent);

        dialog.dismiss();
    }


    @Override
    public void onItemClick(View view, int position) {
        this.position = position;
        dialog.show();
        new Thread(this::checkMovie).start();

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
        switch (item.getItemId()) {
            case R.id.main:
                Intent intent1 = new Intent(this, StartActivity.class);
                startActivity(intent1);
                break;
            case R.id.search:
                Intent intent2 = new Intent(this, Movie_Search.class);
                startActivity(intent2);
                break;
            case R.id.games:
                Intent intent3 = new Intent(this, MINI_Games.class);
                startActivity(intent3);
                break;
            case R.id.invite:
                Intent intent4 = new Intent(this, Invite_Friend.class);
                startActivity(intent4);
                break;
            case R.id.saving:
                Intent intent5 = new Intent(this, My_Saves.class);
                startActivity(intent5);
                break;
            case R.id.music:
                Intent intent6 = new Intent(this, Music.class);
                startActivity(intent6);
                break;
            case R.id.instructions:
                Intent intent7 = new Intent(this, Instructions.class);
                startActivity(intent7);
                break;
            case R.id.about:
                Intent intent8 = new Intent(this, About.class);
                startActivity(intent8);
                break;
            case R.id.exit:
                AlertDialog dialog = yesNo();
                dialog.show();
                break;
        }
        return true;
    }

    public AlertDialog yesNo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            finish();
            finishAffinity();
            dialog.dismiss();
        });
        builder.setNegativeButton("no", (dialog, which) -> dialog.dismiss());
        return builder.create();
    }

    public void goBack(View view) {
        finish();
    }
}