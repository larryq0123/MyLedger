package com.larry.larrylibrary.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Larry on 2018/1/10.
 */

public class InputLengthWatcher implements TextWatcher {

    private String originalString = "";
    private int maxLength;
    private EditText editText;


    public InputLengthWatcher(EditText et, int maxLength){
        editText = et;
        this.maxLength = maxLength;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        originalString = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String changedString = s.toString();
        if (changedString.length() > maxLength) {
            editText.setText(originalString);
            editText.setSelection(editText.length());
        }
    }
}
