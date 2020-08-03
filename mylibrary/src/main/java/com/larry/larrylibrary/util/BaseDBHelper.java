package com.larry.larrylibrary.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDBHelper extends SQLiteOpenHelper {


    private final static String DB_NAME = "GestureTherapy.db";
    private final static int VERSION_CODE = 1;

    private static BaseDBHelper instance;


    public static BaseDBHelper getInstance(Context context){
        if(instance == null){
            synchronized (BaseDBHelper.class){
                if(instance == null){
                    instance = new BaseDBHelper(context);
                }
            }
        }

        return instance;
    }

    private BaseDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
