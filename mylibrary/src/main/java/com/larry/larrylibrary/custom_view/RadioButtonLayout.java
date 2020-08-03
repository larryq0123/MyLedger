package com.larry.larrylibrary.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class RadioButtonLayout extends LinearLayout {

    private RadioGroup radioGroup;

    public RadioButtonLayout(Context context){
        super(context);
    }

    public RadioButtonLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setData(String title, String[] options, int textSize){
        title = title == null ? "   " : title + ":  ";
        TextView textTitle = new TextView(getContext());
        textTitle.setText(title);
        textTitle.setTextColor(Color.BLACK);
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize + 2);
        addView(textTitle);


        radioGroup = new RadioGroup(getContext());
//        radioGroup.setOrientation(LinearLayout.VERTICAL);
        addView(radioGroup);

        for(String option: options){
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(option);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            radioButton.setTextColor(Color.BLACK);
            radioGroup.addView(radioButton);
        }
    }

    public void enableRadioButtons(boolean isEnabled){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View view = radioGroup.getChildAt(i);
            view.setEnabled(isEnabled);
        }
    }

    public int getCheckedPosition(){
        int position = 0;
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View v = radioGroup.getChildAt(i);

            if(v instanceof RadioButton && ((RadioButton) v).isChecked()){
                position = i+1;
            }
        }

        return position;
    }

    public void setCheckedStatus(int position){
        enableRadioButtons(false);
        RadioButton rb = (RadioButton) radioGroup.getChildAt(position-1);
        rb.setChecked(true);
    }
}
