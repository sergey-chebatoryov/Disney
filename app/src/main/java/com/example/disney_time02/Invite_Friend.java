package com.example.disney_time02;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
                if((String.valueOf(edNumber.getText()).equals(""))||(String.valueOf(edMovie.getText()).equals(""))){
                    Toast.makeText(getApplicationContext(), "please insert phone number or movie's name!", Toast.LENGTH_LONG).show();
                }
                else {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.fromParts("sms", String.valueOf(edNumber.getText()), null)).
                            putExtra("sms_body", write()));
                }
            }
        });
    }
    public String write(){
        String text = String.valueOf(edMovie.getText());
        return ("Let's watch together "+'"'+text+'"'+", which is a Disney movie!");

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
        switch (item.getItemId()){
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