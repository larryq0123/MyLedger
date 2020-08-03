package com.larry.larrylibrary.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Larry on 2017/8/25.
 */

public class MeasureUtil {

    /**
     * 以rid取得dimension
     * @param context
     * @param rid
     * @return
     */
    public static int getDimensionFromRid(Context context, int rid) {
        return getDimensionFromRid(context, rid, false);
    }

    public static int getDimensionFromRid(Context context, int rid, boolean asDpSp) {
        if(asDpSp){
            int px = context.getResources().getDimensionPixelSize(rid);
            return pxToDp(context, px);
        }else {
            return context.getResources().getDimensionPixelSize(rid);
        }
    }

    public static DisplayMetrics getMetrics(Context context){
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return context.getResources().getDisplayMetrics();
    }

    public static int dpToPx(Context context, int dp) {
        // 獲取螢幕密度（方法2）
        DisplayMetrics dm = getMetrics(context);
        float density = dm.density;  //螢幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;  //螢幕密度（每寸像素：120/160/240/320）

        return (int) (dp * density);
    }

    public static float getDisplayDensity(Context context){
        DisplayMetrics dm = getMetrics(context);
        return dm.density;  //螢幕密度（像素比例：0.75/1.0/1.5/2.0）
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics dm = getMetrics(context);
        float density = dm.density;  //螢幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;  //螢幕密度（每寸像素：120/160/240/320）

        return (int) (px / density);
    }

    public static int[] getScreenDimens(Context context) {
        DisplayMetrics dm = getMetrics(context);

        int[] dimens = new int[2];
        dimens[0] = dm.widthPixels;
        dimens[1] = dm.heightPixels;
        return dimens;
    }
}
