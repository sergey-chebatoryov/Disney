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
    ArrayList<DataModel> rowsString;
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
            rowsString.add(new DataModel(row.get("name"), row.get("trailer")));
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
        dialog.setTitle("Loading movie...");
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
        MenuClass menuClass = new MenuClass(this);
        return menuClass.getItemSelected(item);
    }

    public void goBack(View view) {
        finish();
    }
}