package com.larry.larrylibrary.util;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        final int width = options.outWidth;
        int inSampleSize = 1;

        BaseLogUtil.logd("ImageUtil", "in calculateInSampleSize, realWidth = "+width+", reqWidth = "+reqWidth);
        if (width > reqWidth) {
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int rId, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, rId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, rId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePathName, int requestWidth){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePathName, options);
        options.inSampleSize = calculateInSampleSize(options, requestWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePathName, options);
    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int requestWidth){
        Bitmap decodedBitmap;

        try {
            InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);
            options.inSampleSize = calculateInSampleSize(options, requestWidth);
            options.inJustDecodeBounds = false;

            imageStream.close();
            imageStream = context.getContentResolver().openInputStream(imageUri);
            decodedBitmap = BitmapFactory.decodeStream(imageStream, null, options);
            imageStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
            decodedBitmap = null;
        }

        return decodedBitmap;
    }

    public static Bitmap getCorrectBitmap(File file, int requestWidth){
        Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), requestWidth);

        int degree = getBitmapDegree(file.getAbsolutePath());
        if(degree != 0){
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    public static Bitmap scaleDownBitmapWithHeight(Bitmap photo, int newHeight) {
        double scaleRatio = newHeight/((double) photo.getHeight());
        int newWidth = (int) (photo.getWidth() * scaleRatio);
        photo = Bitmap.createScaledBitmap(photo, newWidth, newHeight, true);
        return photo;
    }

    public static Bitmap scaleDownBitmapWithWidth(Bitmap photo, int newWidth){
        double scaleRatio = newWidth /((double)photo.getWidth());
        if(scaleRatio < 1) {
            int newHeight = (int) (photo.getHeight() * scaleRatio);
            photo = Bitmap.createScaledBitmap(photo, newWidth, newHeight, true);
        }
        return photo;
    }

    public static Bitmap readBitmapFromStream(Resources resources, int rid, int inSampleSize) {
        if (rid == 0)
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = inSampleSize;
        InputStream is = resources.openRawResource(rid);
        return BitmapFactory.decodeStream(is, null, options);//new BitmapDrawable(resources, BitmapFactory.decodeStream(is, null, options));
    }

    public static Bitmap loadBitmapWithFileName(String fileName){
        File file = new File(FileUtil.getAPPRootDirectory(), fileName);
        return fileToBitmap(file.getAbsolutePath());
    }

    public static void saveBitmapWithFileName(Bitmap bitmap, String fileName){
        if(bitmap != null) {
            File file = new File(FileUtil.getAPPRootDirectory(), fileName);
            saveBitmapToExternalFile(bitmap, file);
        }
    }

    public static String getUniqueFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    public static void saveBitmapToExternalFile(Bitmap bitmap, File file){
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG , 90 , out);
            out.flush ();
            out.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public static Bitmap fileToBitmap(String fileName) {
        Bitmap bitmap;
        if (new File(fileName).exists()) {
            bitmap = BitmapFactory.decodeFile(fileName);
        }
        else {
            Log.e("fileToImageView", fileName + " not found.");
            bitmap = null;
        }
        return bitmap;
    }

    public static byte[] imageFileToByteArray(File imageFile){
        byte[] imageRawData = null;
        try {
            FileInputStream inputStream = new FileInputStream(imageFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            imageRawData = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return imageRawData;
    }

    public static Bitmap takeScreenShot(Activity activity){
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = MeasureUtil.getScreenDimens(activity)[0];
        int height = MeasureUtil.getScreenDimens(activity)[1];
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        view.invalidate();
        return b;
    }

    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {return (null);}

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}
