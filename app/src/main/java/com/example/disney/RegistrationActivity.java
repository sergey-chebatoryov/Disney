package com.example.disney;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mysql.jdbc.MysqlErrorNumbers;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase database;
    private dbHelper dbHelper;
    private Button btnGoReg;
    private EditText etName, etPassword;
    AlertDialog dialog;
    Runnable runnable = () -> {
        Looper.prepare();
        registration();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.dbHelper = new dbHelper(this);
        this.database = this.dbHelper.getWritableDatabase();
        this.database.close();
        this.btnGoReg = findViewById(R.id.btnGoReg);
        this.btnGoReg.setOnClickListener(this);
        this.etName = findViewById(R.id.etName);
        this.etPassword = findViewById(R.id.etPassword);
        if (LoginActivity.userName != null) {
            this.etName.setText(LoginActivity.userName);
            this.etPassword.requestFocus();
        } else {
            this.etName.requestFocus();
        }
        setActionBar("Sign up");
    }

    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(heading);
    }

    @Override
    public void onClick(View v) {
        dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_loading_dialog)
                .setTitle("Registration...").create();
        dialog.show();

        new Thread(runnable).start();
    }

    private void registration() {
        if (isValid()) {
            String[] data = new String[2];
            data[0] = this.etName.getText().toString();
            data[1] = this.etPassword.getText().toString();
            String encryptedPassword = Encryption.encrypt(data[1]);
            this.etPassword.setText("");
            MysqlConnect mysqlConnect = new MysqlConnect();
            int result = mysqlConnect.executeSql("insert into users (name, password) values ('"
                    + data[0] + "', '" + encryptedPassword + "')", this);
            processSqlResult(data[0], result);
        } else {
            Toast.makeText(RegistrationActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    private void processSqlResult(String name, int result) {
        if (result == 1) {//new user inserted
            LoginActivity.userName = name;
            Toast.makeText(this, "You sign up", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (result == MysqlErrorNumbers.ER_DUP_ENTRY) {//Duplication error
            Toast.makeText(RegistrationActivity.this, "User already exists. Try Again", Toast.LENGTH_SHORT).show();
        } else if (result == MysqlErrorNumbers.ER_ACCESS_DENIED_ERROR) {//Access denied to database
            Toast.makeText(RegistrationActivity.this, "Access to database is denied. Call to administrator", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationActivity.this, "User does not created. Try Again", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isFound(String name, String password) {
        boolean flag = false;
        this.database = this.dbHelper.getWritableDatabase();
        Cursor cursor = database.query(this.dbHelper.TABLE_NAME, null, null, null, null, null, null);

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

    private boolean isEmpty() {
        this.database = this.dbHelper.getWritableDatabase();
        int count = 0;
        Cursor cursor = this.database.query(this.dbHelper.TABLE_NAME, null, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && count == 0) {
            count++;
            cursor.moveToNext();
        }
        cursor.close();
        return count == 0;
    }

    private boolean isValid() {
        String message = "";
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();
        if (name.equals("")) {
            message += "The field 'name' is empty!\nInsert data!";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            message = "";
            return false;
        }
        if (password.equals("")) {
            message += "The field 'password' is empty!\nInsert data!";
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
        ShortMenuClass shortMenuClass = new ShortMenuClass(this);
        return shortMenuClass.getItemSelected(item);
    }
}