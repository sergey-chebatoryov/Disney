package com.example.disney_time02;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class R_role extends AppCompatActivity {
    private Button btnPress;
    private ImageView imageVariable;
    private Button btnTryAgain, back;
    private TextView text;
    private enum variables {simba, scar, timon, nala, mufasa};
    private enum result {answer};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrole);
        setActionBar("");
        this.btnPress = findViewById(R.id.press);
        this.imageVariable = findViewById(R.id.imageVariable);
        this.btnTryAgain = findViewById(R.id.tryagain);
        this.back = findViewById(R.id.back);
        this.text = findViewById(R.id.text);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                imageVariable.setImageResource(R.drawable.question);
                text.setText(null);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printResult();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    public void printResult() {
        int num= (int)(Math.random() * 5);
        result result = getResult(makeShape(num),num);
    }
    @SuppressLint("SetTextI18n")
    public result getResult(variables variable, int num) {
        printShape(variable);
        text.setText(makeName(num));
        return result.answer;
    }
    public String makeName(int number) {
        switch (number) {
            case 0:
                return "The main character";
            case 1:
                return "The antagonists against the main character";
            case 2:
                return "Secondary character";
            case 3:
                return "The one that the main character is in love with";
            default:
                return "The one that symbolizes the genre";
        }
    }
    public variables makeShape(int number) {
        switch (number) {
            case 0:
                return variables.simba;
            case 1:
                return variables.scar;
            case 2:
                return variables.timon;
            case 3:
                return variables.nala;
            default:
                return variables.mufasa;
        }
    }

    public void printShape(variables variable) {
        switch (variable) {
            case simba:
                imageVariable.setImageResource(R.drawable.simba);
                break;
            case scar:
                imageVariable.setImageResource(R.drawable.scar);
                break;
            case timon:
                imageVariable.setImageResource(R.drawable.timon);
                break;
            case nala:
                imageVariable.setImageResource(R.drawable.nala);
                break;
            default:
                imageVariable.setImageResource(R.drawable.mufasa);
        }
    }
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(androidx.cardview.R.color.cardview_dark_background, null)));
        actionBar.setTitle(heading);
    }
}