package com.larry.larrylibrary.custom_view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.larry.larrylibrary.R;


public class OneButtonMessageWindow extends PopupWindow implements View.OnClickListener{

    private View parentView;
    private TextView textMessage;
    private TextView textButton;
    private String message;
    private String buttonString;
    private int gravity;


    public interface OneButtonWindowListener {
        void onOneButtonWindowClicked();
    }

    private OneButtonWindowListener oneButtonWindowListener;

    public OneButtonMessageWindow(Activity a, View v, String message) {
        this(a, v, message, "確認");
    }

    public OneButtonMessageWindow(Activity a, View v, String message, String buttonString) {
        this(a, v, message, buttonString, Gravity.CENTER);
    }

    public OneButtonMessageWindow(Activity a, View v, String message, String buttonString, int gravity) {
        parentView = v;
        this.message = message;
        this.buttonString = buttonString;
        this.gravity = gravity;
        setPopupWindow(a);
    }

    private void setPopupWindow(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.window_one_button_message, null);
        setContentView(view);

        textMessage = (TextView) view.findViewById(R.id.text_message);
        textButton = (TextView) view.findViewById(R.id.text_button);
        textButton.setOnClickListener(this);
        textMessage.setText(message);
        textMessage.setGravity(gravity);
        textButton.setText(buttonString);


        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);
    }

    public void setTextSize(int size){
        textMessage.setTextSize(size);
        textButton.setTextSize(size);
    }

    public void show() {
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
    }

    public void setOneButtonWindowListener(OneButtonWindowListener l) {
        oneButtonWindowListener = l;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        oneButtonWindowListener = null;
    }

    @Override
    public void onClick(View v) {
        if (oneButtonWindowListener != null) {
            oneButtonWindowListener.onOneButtonWindowClicked();
        }

        dismiss();
    }
}