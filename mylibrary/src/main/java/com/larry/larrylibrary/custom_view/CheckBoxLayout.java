package com.larry.larrylibrary.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.larry.larrylibrary.util.BaseLogUtil;
import com.larry.larrylibrary.util.MeasureUtil;


public class CheckBoxLayout extends LinearLayout{

    private LinearLayout linearOptions;
    private CheckBox checkBoxOther;
    private EditText editOther;

    public CheckBoxLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }



    public void setData(String title, String[] options, int textSize){
        setData(title, options, textSize, false);
    }

    public void setData(String title, String[] options, int textSize, boolean hasOtherOption){
        title = title == null ? "   " : title + ":  ";
        TextView textTitle = new TextView(getContext());
        textTitle.setText(title);
        textTitle.setTextColor(Color.BLACK);
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize + 2);
        addView(textTitle);

        linearOptions = new LinearLayout(getContext());
        linearOptions.setOrientation(LinearLayout.VERTICAL);
        addView(linearOptions);

        for(String option: options){
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(option);
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setEnabled(false);
            linearOptions.addView(checkBox);
        }

        if(hasOtherOption){
            checkBoxOther = new CheckBox(getContext());
            checkBoxOther.setText("其他");
            checkBoxOther.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            checkBoxOther.setTextColor(Color.BLACK);
            checkBoxOther.setEnabled(false);
            linearOptions.addView(checkBoxOther);

            editOther = new EditText(getContext());
            LayoutParams lp = new LayoutParams(MeasureUtil.dpToPx(getContext(), 150), ViewGroup.LayoutParams.WRAP_CONTENT);
            editOther.setLayoutParams(lp);
            lp.leftMargin = MeasureUtil.dpToPx(getContext(), 15);
            editOther.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            editOther.setTextColor(Color.BLACK);
            editOther.setEnabled(false);
            linearOptions.addView(editOther);

            checkBoxOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editOther.setEnabled(isChecked);
                }
            });
        }
    }

    public void enableCheckBoxes(boolean isEnabled){
        for (int i = 0; i < linearOptions.getChildCount(); i++) {
            View view = linearOptions.getChildAt(i);

            if(view instanceof EditText) {
                enableEditOther(isEnabled);
            }else {
                view.setEnabled(isEnabled);
            }
        }
    }

    private void enableEditOther(boolean isEnabled){
        editOther.setEnabled(isEnabled && checkBoxOther.isEnabled() && checkBoxOther.isChecked());
    }

    public String getCheckedItems(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < linearOptions.getChildCount(); i++) {
            View v = linearOptions.getChildAt(i);

            if(v instanceof CheckBox && ((CheckBox) v).isChecked()){
                sb.append(String.valueOf(i+1) + ",");
            }
        }

        String items = sb.toString();
        if(items.endsWith(",")){
            items = items.substring(0, items.length()-1);
        }else if(items.equals("")){
            items = "0";
        }

        BaseLogUtil.logd("CheckBox", items);
        return items;
    }

    public String getCheckedItemsString(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < linearOptions.getChildCount(); i++) {
            View v = linearOptions.getChildAt(i);

            if(v instanceof CheckBox && v != checkBoxOther){
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()) {
                    sb.append(cb.getText().toString().trim() + ",");
                }
            }
        }

        if(checkBoxOther.isChecked()){
            sb.append(editOther.getText().toString().trim());
        }

        String items = sb.toString();
        if(items.endsWith(",")){
            items = items.substring(0, items.length()-1);
        }

        BaseLogUtil.logd("CheckBox", items);
        return items;
    }

    public void setCheckedStatus(String checkedPosition){
        enableCheckBoxes(false);
        int[] positions;

        if(checkedPosition.contains(",")){
            String[] splitPositions = checkedPosition.split(",");
            positions = new int[splitPositions.length];
            for (int i = 0; i < splitPositions.length; i++) {
                positions[i] = Integer.parseInt(splitPositions[i]);
            }
        }else if(!checkedPosition.equals("0")){
            positions = new int[1];
            positions[0] = Integer.parseInt(checkedPosition);
        }else {
            positions = new int[0];
        }

        for(int i: positions){
            CheckBox checkBox = (CheckBox) linearOptions.getChildAt(i-1);
            checkBox.setChecked(true);
        }
    }

    public void setCheckedStatusInString(String checkedOptions){
        enableCheckBoxes(false);
        String[] options;

        if(checkedOptions.contains(",")){
            options = checkedOptions.split(",");
        }else {
            options = new String[1];
            options[0] = checkedOptions;
        }

        for(String option: options){
            boolean hasMatch = false;
            for (int i = 0; i < linearOptions.getChildCount(); i++) {
                View v = linearOptions.getChildAt(i);
                if(v instanceof CheckBox && v != checkBoxOther){
                    CheckBox cb = (CheckBox) v;
                    if(cb.getText().toString().equals(option)){
                        cb.setChecked(true);
                        hasMatch = true;
                    }
                }
            }

            if(!hasMatch){
                checkBoxOther.setChecked(true);
                editOther.setText(option);
            }
        }

        editOther.setEnabled(false);
    }
}
