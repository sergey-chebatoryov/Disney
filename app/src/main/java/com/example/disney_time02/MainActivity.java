package com.example.disney_time02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button btnLog, btnReg;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnLog = findViewById(R.id.btnLog);
        this.btnReg = findViewById(R.id.btnReg);
        setActionBar("Welcome to Disney Time!");

        //Check saved credentials
        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .setTitle("Login...").create();
        dialog.show();
        new Thread(this::checkCredentials).start();
    }

    private void checkCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        String passwordHash = sharedPreferences.getString("passwordHash", "");
        if (!Objects.equals(userName, "")) {
            MysqlConnect mysqlConnect = new MysqlConnect();
            String resultSelectUser = mysqlConnect.selectColumn("select * from users where name='" + userName + "'",
                    "password", this);
            if (resultSelectUser != null && Objects.equals(passwordHash, resultSelectUser)) {
                LoginActivity.userName = userName;
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
        dialog.dismiss();
    }

    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(androidx.cardview.R.color.cardview_dark_background, null)));
        actionBar.setTitle(heading);
    }

    public void goReg(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        finish();
    }
}