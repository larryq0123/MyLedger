package com.larry.larrylibrary.custom_view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.larry.larrylibrary.R;


public class TwoButtonMessageWindow extends PopupWindow implements View.OnClickListener{

    private View parentView;
    private String message;
    private String buttonLeftString;
    private String buttonRightString;


    public interface TwoButtonWindowListener {
        void onTwoButtonWindowClicked(boolean isLeftClicked);
    }

    private TwoButtonWindowListener twoButtonWindowListener;

    public TwoButtonMessageWindow(Activity a, View v, String message) {
        this(a, v, message, "確認", "取消");
    }

    public TwoButtonMessageWindow(Activity a, View v, String message, String leftString, String rightString) {
        parentView = v;
        this.message = message;
        buttonLeftString = leftString;
        buttonRightString = rightString;
        setPopupWindow(a);
    }


    private void setPopupWindow(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.window_two_button_message, null);
        setContentView(view);

        TextView textMessage = (TextView) view.findViewById(R.id.text_message);
        TextView textButtonLeft = (TextView) view.findViewById(R.id.text_button_left);
        TextView textButtonRight = (TextView) view.findViewById(R.id.text_button_right);
        textButtonLeft.setOnClickListener(this);
        textButtonRight.setOnClickListener(this);
        textMessage.setText(message);
        textButtonLeft.setText(buttonLeftString);
        textButtonRight.setText(buttonRightString);


        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);
    }

    public void show() {
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
    }

    public void setTwoButtonWindowListener(TwoButtonWindowListener l) {
        twoButtonWindowListener = l;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        twoButtonWindowListener = null;
    }

    @Override
    public void onClick(View v) {
        if (twoButtonWindowListener != null) {
            twoButtonWindowListener.onTwoButtonWindowClicked(v.getId() == R.id.text_button_left);
        }

        dismiss();
    }
}