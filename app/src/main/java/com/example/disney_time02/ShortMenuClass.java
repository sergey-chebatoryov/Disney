package com.example.disney_time02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ShortMenuClass  extends AppCompatActivity {
    private final Context context;

    public ShortMenuClass(Context context) {
        this.context = context;
    }

    public boolean getItemSelected(final MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.main_start:
                intent = new Intent(context, MainActivity.class);
                break;
            case R.id.login:
                intent = new Intent(context, LoginActivity.class);
                break;
            case R.id.reg:
                intent = new Intent(context, RegistrationActivity.class);
                break;
            case R.id.exit:
                AlertDialog dialog = yesNo();
                dialog.show();
                break;
        }
        if (intent != null) {
            context.startActivity(intent);
        }
        return true;
    }

    public AlertDialog yesNo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            ((Activity) context).finish();
            ((Activity) context).finishAffinity();
            dialog.dismiss();
        });
        builder.setNegativeButton("no", (dialog, which) -> dialog.dismiss());
        return builder.create();
    }

}
