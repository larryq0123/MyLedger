package com.larry.larrylibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.List;

public class MultiAppUtil {

    private static String[] suspectedFilePath = new String[]{
            "virtual",
            "parallel",
            "multi",
            "master",
            "god",
            "copy",
            "clone",
            "double",
            "account"
    };

    public static boolean hasMultiApp(Context context, String applicationName){
        return hasDuplicatePackageName(context, applicationName) || hasSuspectedFilePath(context);
    }

    private static boolean hasDuplicatePackageName(Context context, String applicationName){
        try{
            int count = 0;
            List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
            for(PackageInfo packageInfo: packages){
                if(packageInfo.packageName.contains(applicationName))
                    count++;
            }

            return count > 1;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private static boolean hasSuspectedFilePath(Context context){
        try{
            String filePath = context.getFilesDir().getAbsolutePath();
            for(String path: suspectedFilePath){
                if(filePath.contains(path))
                    return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
