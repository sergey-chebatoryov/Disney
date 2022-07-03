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

public class R_food extends AppCompatActivity {
    private Button btnPress;
    private ImageView imageVariable;
    private Button btnTryAgain, back;
    private TextView text;
    private enum variables {popcorn, hot_dog, ice_cream, pancake, gummy, nachos};
    private enum result {answer};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfood);
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
        int num= (int)(Math.random() * 6);
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
                return "popcorn";
            case 1:
                return "hot dog";
            case 2:
                return "ice cream";
            case 3:
                return "gummy";
            case 4:
                return "nachos";
            default:
                return "pancake";
        }
    }
    public variables makeShape(int number) {
        switch (number) {
            case 0:
                return variables.popcorn;
            case 1:
                return variables.hot_dog;
            case 2:
                return variables.ice_cream;
            case 3:
                return variables.gummy;
            case 4:
                return variables.nachos;
            default:
                return variables.pancake;
        }
    }

    public void printShape(variables variable) {
        switch (variable) {
            case popcorn:
                imageVariable.setImageResource(R.drawable.popcorn);
                break;
            case hot_dog:
                imageVariable.setImageResource(R.drawable.hot_dog);
                break;
            case ice_cream:
                imageVariable.setImageResource(R.drawable.ice_cream);
                break;
            case gummy:
                imageVariable.setImageResource(R.drawable.gummy);
                break;
            case nachos:
                imageVariable.setImageResource(R.drawable.nachos);
                break;
            default:
                imageVariable.setImageResource(R.drawable.pancake);
        }
    }
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(androidx.cardview.R.color.cardview_dark_background, null)));
        actionBar.setTitle(heading);
    }
}