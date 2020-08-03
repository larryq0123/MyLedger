package com.larry.larrylibrary.custom_view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.larry.larrylibrary.R;

public class ProgressWindow extends PopupWindow {

    private View parentView;
    private boolean isReadyToDismiss = false;
    private TextView messageText;


    public ProgressWindow(Activity a, View v) {
        parentView = v;
        setPopupWindow(a);
    }


    private void setPopupWindow(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.window_progress, null);
        messageText = (TextView) view.findViewById(R.id.message_text);

        setContentView(view);

        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);

        setBackgroundDrawable(new BitmapDrawable(activity.getResources(), Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)));
    }

    public void show() {
        messageText.setVisibility(View.GONE);
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
    }

    public void showMessage(String message){
        messageText.setText(message);
        messageText.setVisibility(View.VISIBLE);
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
    }

    public void dismissWindow(){
        isReadyToDismiss = true;
        dismiss();
    }

    @Override
    public void dismiss() {
        if(isReadyToDismiss) {
            super.dismiss();
            isReadyToDismiss = false;
        }
    }


}
