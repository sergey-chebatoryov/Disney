package com.example.disney;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class DialogMultiplyNumbers extends AppCompatActivity {
    Map<String, String> numberType;
    private final EditText edNumber;
    private final Context context;

    public DialogMultiplyNumbers(Map<String, String> numberType, EditText edNumber, Context context) {
        this.numberType = numberType;
        this.edNumber = edNumber;
        this.context = context;
    }
    @SuppressLint("SetTextI18n")
    public void makeDialogForMultipleNumbers(String name) {
        if (numberType.size() == 1) {
            edNumber.setText(numberType.entrySet().iterator().next().getValue());
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_multiple_numbers);
        RadioGroup radioGroup = dialog.findViewById(R.id.multipleNumberGroup);
        TextView displayName = dialog.findViewById(R.id.displayName);
        displayName.setText(name);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lWindowParams);

        numberType.keySet().forEach(key -> {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(numberType.get(key) + "\n" + key);
            radioButton.setPadding(0, 15, 0, 15);
            radioGroup.addView(radioButton);
        });
        dialog.show();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadio = group.findViewById(group.getCheckedRadioButtonId());
            String num = checkedRadio.getText().toString();
            edNumber.setText(num.substring(0, num.indexOf('\n')));
            dialog.dismiss();
        });
    }
}
