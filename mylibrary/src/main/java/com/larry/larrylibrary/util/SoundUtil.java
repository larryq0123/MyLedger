package com.larry.larrylibrary.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Larry on 2017/8/23.
 */

public class SoundUtil {

    private static MediaPlayer player;


    public static void playShortMusic(Context context, int rawID) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null && audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            final MediaPlayer mp = MediaPlayer.create(context, rawID);
            if (mp != null) {
                try {
                    mp.start();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    BaseLogUtil.loge("SoundUtil", "playShortMusic: exception caught");
                }
            }
        }
    }

    public static void prepareMusic(Context context, int rawID) throws IOException {
        stopMusic();

        if(player == null){
            player = new MediaPlayer();
        }else {
            player.reset();
        }

        AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawID);
        player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        player.prepare();
    }

    public static void playMusic(){
        if(player != null){
            player.start();
        }
    }

    public static void stopMusic(){
        if(player != null){
            player.stop();
        }
    }

//    /**
//     * 取得目前音量隊最大音量的比率
//     * @return 0 ~ 1f
//     */
//    public static float getVolumeRatio(Context context){
//        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        float max = am.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
//        int current = am.getStreamVolume( AudioManager.STREAM_SYSTEM );
//        //BaseLogUtil.logd(TAG, "max = " + max + ", current = " + current);
//
//        return current / max;
//    }
}
