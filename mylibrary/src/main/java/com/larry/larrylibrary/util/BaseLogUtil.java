package com.larry.larrylibrary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class BaseLogUtil {
    private static Toast toast = null;

    public static void logLifeCycle(String TAG, String message){
        Log.d(TAG, message);
    }

    public static void logd(String TAG, String message, Exception e){
        logd(TAG, message + ", exception = " + e.toString());
    }

    public static void logd(String TAG, String message) {
        if (message.length() > 3000) {
            int chunkCount = message.length() / 3000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 3000 * (i + 1);
                if (max >= message.length()) {
                    Log.d(TAG, "chunk " + i + " of " + chunkCount + ":*** " + message.substring(3000 * i));
                } else {
                    Log.d(TAG, "chunk " + i + " of " + chunkCount + ":*** " + message.substring(3000 * i, max));
                }
            }
        } else {
            Log.d(TAG, message);
        }
    }

    public static void loge(String TAG, String message, Exception e){
        loge(TAG, message + ", exception = " + e.toString());
    }

    public static void loge(String TAG, String message) {
        if (message.length() > 3000) {
            int chunkCount = message.length() / 3000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 3000 * (i + 1);
                if (max >= message.length()) {
                    Log.e(TAG, "chunk " + i + " of " + chunkCount + ":*** " + message.substring(3000 * i));
                } else {
                    Log.e(TAG, "chunk " + i + " of " + chunkCount + ":*** " + message.substring(3000 * i, max));
                }
            }
        } else {
            Log.e(TAG, message);
        }
    }

    @SuppressLint("ShowToast")
    public static void showToast(Context mContext, String text) {
        if (toast == null) {
            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }

        toast.show();
    }

    @SuppressLint("ShowToast")
    public static void showToast(Context mContext, int textRid) {
        if (toast == null) {
            toast = Toast.makeText(mContext, textRid, Toast.LENGTH_SHORT);
        } else {
            toast.setText(textRid);
        }

        toast.show();
    }
}
