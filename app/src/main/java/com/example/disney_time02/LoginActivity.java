package com.example.disney_time02;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private SQLiteDatabase database;
    private dbHelper dbHelper;
    private EditText etName, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.dbHelper = new dbHelper(this);
        this.database = this.dbHelper.getWritableDatabase();
        this.database.close();
        Button btnGoLog = findViewById(R.id.btnGoLogin);
        btnGoLog.setOnClickListener(this);
        this.etName = findViewById(R.id.etNameLogin);
        this.etPassword = findViewById(R.id.etPasswordLogin);
        setActionBar("Sign in");
    }
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(heading);
    }
    @Override
    public void onClick(View view) {
        String[] data = new String[2];
        data[0] = this.etName.getText().toString();
        data[1] = this.etPassword.getText().toString();
        if (isValid()) {
            if (isEmpty()) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return;
            }
            if (!isFound(data[0], data[1])) {
                Toast.makeText(this, "User not found, try again!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Welcome my friend!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
            }
        }
        this.etName.setText("");
        this.etPassword.setText("");
    }
    private boolean isEmpty(){
        this.database = this.dbHelper.getWritableDatabase();
        int count = 0;
        Cursor cursor = this.database.query(this.dbHelper.TABLE_NAME, null, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && count == 0){
            count++;
            cursor.moveToNext();
        }
        cursor.close();
        return count == 0;
    }
    private boolean isFound(String name, String password){
        boolean flag = false;
        this.database = this.dbHelper.getWritableDatabase();
        Cursor cursor = database.query(this.dbHelper.TABLE_NAME, null, null, null, null,
                null, null);
        int column1 = cursor.getColumnIndex(this.dbHelper.USERNAME);
        int column2 = cursor.getColumnIndex(this.dbHelper.PASSWORD);
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && !flag) {
            String baseName = cursor.getString(column1);
            String basePassword = cursor.getString(column2);
            flag = name.equals(baseName) && password.equals(basePassword);
            cursor.moveToNext();
        }
        cursor.close();
        this.database.close();
        return flag;
    }
    private boolean isValid(){
        String message = "";
        String name = this.etName.getText().toString();
        String password = this.etPassword.getText().toString();
        if (name==null || name.equals("")){
            message+="The field 'name' is empty!\nInsert data!";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            message = "";
            return false;
        }
        if (password==null || password.equals("")){
            message+="The field 'password' is empty!\nInsert data!";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            message = "";
            return false;
        }
        return true;
    }
    public void goBack(View view) {
        finish();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_start:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.login:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.reg:
                Intent intent3 = new Intent(this, RegistrationActivity.class);
                startActivity(intent3);
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
}