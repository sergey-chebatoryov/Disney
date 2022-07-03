package com.example.disney;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class R_character extends AppCompatActivity {
    private Button btnPress;
    private ImageView imageVariable;
    private Button btnTryAgain, back;
    private TextView text;
    private enum variables {ariel, aurora, belle, cinderella, mickey, aladdin, flounder};
    private enum result {answer};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcharacter);
        setActionBar("");
        this.btnPress = findViewById(R.id.press);
        this.imageVariable = findViewById(R.id.imageVariable);
        this.btnTryAgain=findViewById(R.id.tryagain);
        this.back=findViewById(R.id.back);
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
        int num= (int)(Math.random() * 7);
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
                return "Princess Ariel";
            case 1:
                return "Princess Aurora";
            case 2:
                return "Princess Cinderella";
            case 3:
                return "Mickey";
            case 4:
                return "Aladdin";
            case 5:
                return "flounder";
            default:
                return "Princess Belle";
        }
    }
    public variables makeShape(int number) {
        switch (number) {
            case 0:
                return variables.ariel;
            case 1:
                return variables.aurora;
            case 2:
                return variables.cinderella;
            case 3:
                return variables.mickey;
            case 4:
                return variables.aladdin;
            case 5:
                return variables.flounder;
            default:
                return variables.belle;
        }
    }

    public void printShape(variables variable) {
        switch (variable) {
            case ariel:
                imageVariable.setImageResource(R.drawable.ariel);
                break;
            case aurora:
                imageVariable.setImageResource(R.drawable.aurora);
                break;
            case cinderella:
                imageVariable.setImageResource(R.drawable.cinderella);
                break;
            case mickey:
                imageVariable.setImageResource(R.drawable.mickey);
                break;
            case aladdin:
                imageVariable.setImageResource(R.drawable.aladdin);
                break;
            case flounder:
                imageVariable.setImageResource(R.drawable.flounder);
                break;
            default:
                imageVariable.setImageResource(R.drawable.belle);
        }
    }
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(androidx.cardview.R.color.cardview_dark_background, null)));
        actionBar.setTitle(heading);
    }
}