package com.larry.larrylibrary.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.larry.larrylibrary.R;

/**
 * Created by Larry on 2018/2/9.
 */

public class ProgressDialog extends Dialog{

    private boolean isCancelable;
    private boolean isReadyToCancel = false;

    public ProgressDialog(Context context, boolean isCancelable){
        this(context, "", isCancelable);
    }

    public ProgressDialog(@NonNull Context context, String message, boolean isCancelable) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        setContentView(R.layout.dialog_progress);

        TextView textMessage = (TextView) findViewById(R.id.text_message);
        textMessage.setText(message);
        this.isCancelable = isCancelable;
    }

    public void dismissDialog(){
        isReadyToCancel = true;
        dismiss();
    }

    @Override
    public void dismiss() {
        if(isReadyToCancel || isCancelable){
            super.dismiss();
        }
    }
}
