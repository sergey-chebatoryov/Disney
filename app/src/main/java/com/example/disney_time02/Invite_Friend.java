package com.example.disney_time02;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Invite_Friend extends AppCompatActivity/* implements AdapterView.OnItemSelectedListener */{
    private EditText edNumber;
    private Spinner spinner;
    AlertDialog dialog;
    List<String> rowStrings;
    ArrayAdapter<String> adapter;
    Map<Integer, Map<String, String>> moviesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        setActionBar("");
        Button btnMessage = findViewById(R.id.message);
        this.edNumber = findViewById(R.id.number);

        spinner = findViewById(R.id.movie);
        rowStrings = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rowStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .setTitle("Searching movies...").create();

        btnMessage.setOnClickListener(v -> {
            if ((String.valueOf(edNumber.getText()).equals("")) || (spinner.getSelectedItem().equals(""))) {
                Toast.makeText(getApplicationContext(), "please insert phone number or movie's name!", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.fromParts("sms", String.valueOf(edNumber.getText()), null)).
                        putExtra("sms_body", write()));
            }
        });

        dialog.show();
        new Thread(this::selectMovie).start();
    }

    private void selectMovie() {
        MysqlConnect mysqlConnect = new MysqlConnect();
        moviesMap = mysqlConnect.select("SELECT b.id, a.name, a.genre, trailer FROM movies a JOIN usersmovie b ON b.id=a.id where b.name='" + LoginActivity.userName + "'", this);
        Collection<Map<String, String>> rows = moviesMap.values();
        for (Map<String, String> row : rows) {
            rowStrings.add(row.get("name"));
        }
        this.runOnUiThread(() -> adapter.notifyDataSetChanged());

        dialog.dismiss();
    }


    public String write() {
        String text = spinner.getSelectedItem().toString();
        Map<String, String> row = moviesMap.get(spinner.getSelectedItemPosition());
        String url = Objects.requireNonNull(row).get("trailer");
        return ("Let's watch together " + '"' + text + '"' + ", which is a Disney movie! " + url);

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