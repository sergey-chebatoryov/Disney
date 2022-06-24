package com.example.disney_time02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MenuClass extends AppCompatActivity {
    private final Context context;

    public MenuClass(Context context) {
        this.context = context;
    }

    public boolean getItemSelected(final MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.main:
                intent = new Intent(context, StartActivity.class);
                break;
            case R.id.search:
                intent = new Intent(context, Movie_Search.class);
                break;
            case R.id.games:
                intent = new Intent(context, MINI_Games.class);
                break;
            case R.id.invite:
                intent = new Intent(context, Invite_Friend.class);
                break;
            case R.id.saving:
                intent = new Intent(context, My_Saves.class);
                break;
            case R.id.music:
                intent = new Intent(context, Music.class);
                break;
            case R.id.instructions:
                intent = new Intent(context, Instructions.class);
                break;
            case R.id.about:
                intent = new Intent(context, About.class);
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
//            logout();
        });
        builder.setNegativeButton("no", (dialog, which) -> dialog.dismiss());
        return builder.create();
    }

    public void logout() {
        //Clear password
        context.getSharedPreferences("credentials", MODE_PRIVATE)
                .edit()
                .putString("passwordHash", "")
                .apply();
        //Return to login page with flag as new task
      context.startActivity(new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
