package com.larry.larrylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;


public class GlobalUtil {
    static final String APP_NAME = "123Coin";

    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final String REGEX_PASSWORD = "^(?!.*[^a-zA-Z0-9])(?=.*\\d)(?=.*[a-zA-Z]).{8,}$";


    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    public static boolean isEmail(String email) {
        return isMatch(REGEX_EMAIL, email);
    }

    public static boolean isPassword(String password) {
        return isMatch(REGEX_PASSWORD, password);
    }

    //------------------------------------------------------------------------------------------------

    public static boolean isAPPInDebug(Context context){
        try{
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }catch (Exception e){
            return false;
        }
    }

    public static int getVersionCode(Context context){
        int localVersion = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return localVersion;
    }

    @Nullable
    public static String getVersionName(Context context){
        String localVersion = null;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return localVersion;
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getString(Context context, int id){
        return context.getString(id);
    }

    public static String utf8ToString(byte[] bytes) {
        String asd = "";
        try {
            asd = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asd;
    }

    public static String big5ToUnicode(String strBIG5) {
        String strReturn = "";
        try {
            strReturn = new String(strBIG5.getBytes("big5"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }

    public static String unicodeToBig5(String strUTF8) {
        String strReturn = "";
        try {
            strReturn = new String(strUTF8.getBytes("UTF-8"), "big5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }

    public static String getSerialID() {
        String SerialID = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            SerialID = Build.SERIAL;
        }
        return SerialID;
    }

    public static void changeLanguageLocale(Context context, Locale locale){
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    public static int getRandomInt(int range){
        Random r = new Random();
        return r.nextInt(range);
    }
}
