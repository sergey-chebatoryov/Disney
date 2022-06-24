package com.example.disney_time02;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Invite_Friend extends AppCompatActivity {
    private Button btnMessage;
    private EditText edNumber, edMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        setActionBar("");
        this.btnMessage = findViewById(R.id.message);
        this.edNumber = findViewById(R.id.number);
        this.edMovie = findViewById(R.id.movie);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((String.valueOf(edNumber.getText()).equals("")) || (String.valueOf(edMovie.getText()).equals(""))) {
                    Toast.makeText(getApplicationContext(), "please insert phone number or movie's name!", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.fromParts("sms", String.valueOf(edNumber.getText()), null)).
                            putExtra("sms_body", write()));
                }
            }
        });
    }

    public String write() {
        String text = String.valueOf(edMovie.getText());
        return ("Let's watch together " + '"' + text + '"' + ", which is a Disney movie!");

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