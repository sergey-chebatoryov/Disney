package com.example.disney_time02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Invite_Friend extends AppCompatActivity {
    public static final int REQUEST_READ_CONTACTS = 79;
    private EditText edNumber;
    private Spinner spinner;
    AlertDialog dialog;
    List<String> movies;
    ArrayAdapter<String> adapterMovie;
    Map<Integer, Map<String, String>> moviesMap;
    Map<String, String> numberType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        setActionBar("");
        Button btnMessage = findViewById(R.id.message);

        //Create dropdown box for movie
        spinner = findViewById(R.id.movie);
        movies = new ArrayList<>();
        adapterMovie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, movies);
        adapterMovie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterMovie);

        edNumber = findViewById(R.id.number);

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
            movies.add(row.get("name"));
        }
        this.runOnUiThread(() -> adapterMovie.notifyDataSetChanged());

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

    //On click to edit box 'number' call telephone book
    public void goContacts(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            mStartForResult.launch(contactPickerIntent);
        } else {
            requestPermission();
        }

    }

    //Request permission to read contacts
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            runPermissionDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                mStartForResult.launch(contactPickerIntent);
            }
        }
    }

    //If permission denied ask to set permission manually
    private void runPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grant Permission");
        builder.setMessage(" Read contacts permission denied.\nDo you want to grant it?");
        builder.setPositiveButton("yes", (dialog, which) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                    dialog.dismiss();
                })
                .setNegativeButton("no", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    //Read phone book call result
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("Range")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Uri contactData = intent != null ? intent.getData() : null;
                        ContentResolver cr = getContentResolver();
                        Cursor cur = cr.query(contactData, null, null, null, null);
                        if (cur.getCount() > 0) {
                            if (cur.moveToNext()) {
                                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                    // Query phone here.
                                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                    numberType = new HashMap<>();
                                    while (phones.moveToNext()) {
                                        String phoneNum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        String type = ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(), phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)), "").toString();
                                        numberType.put(type, phoneNum);
                                    }
                                    //Show dialog to choose number for multi-number contact
                                    DialogMultiplyNumbers dialogMultiplyNumbers = new DialogMultiplyNumbers(numberType, edNumber, Invite_Friend.this);
                                    dialogMultiplyNumbers.makeDialogForMultipleNumbers(name);
                                    phones.close();
                                }
                            }
                        }
                        cur.close();
                    }
                }
            });

    public void goBack(View view) {
        finish();
    }
}