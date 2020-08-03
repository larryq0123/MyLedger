package com.larry.larrylibrary.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileUtil{
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getPublicAlbumStorageDir(String albumName) {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(pictures, albumName);

        if (!file.mkdirs()) {
            Log.e("getAlbumStorageDir", "Directory not created");
        }

        return file;
    }

    public static File getAlbumStorageDir(Context context, String albumName) {
        File pictures = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(pictures, albumName);

        if(!file.exists()) {
            if (!file.mkdirs()) {
                file = null;
                Log.e("getAlbumStorageDir", "Directory not created");
            }
        }

        return file;
    }

    public static File getAPPRootDirectory(){
        File rootDirectory = new File(Environment.getExternalStorageDirectory(), GlobalUtil.APP_NAME);
        if(!rootDirectory.exists()){
            rootDirectory.mkdir();
        }

        return rootDirectory;
    }

    public static File getFileUnderAPPRootDirectory(String fileName){
        return new File(getAPPRootDirectory(), fileName);
    }

    public static void showFileInDesktop(Context context, File file){
        MediaScannerConnection.scanFile(context, new String[] {file.getAbsolutePath()}, new String[] {"image/jpeg"}, null);
    }
}
