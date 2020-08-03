package com.larry.larrylibrary.util;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by Larry on 2017/8/22.
 * 簡單的計時器, 提供格式化的時間字串
 */
public class SimpleTimer {
    private boolean isTimerRunning = false;
    //以下三個計算經過多少時間用
    private long startTime;
    private long elapsedTime;
    private long offsetTime;

    private Handler handler;
    private WatchRunnable watchRunnable;
    private String formatTimeString;
    private SimpleTimerListener mListener;


    public SimpleTimer(SimpleTimerListener l){
        mListener = l;
    }

    /**
     * 碼錶歸零
     */
    public void zeroWatch() {
        isTimerRunning = false;
        startTime = 0;
        elapsedTime = 0;
        offsetTime = 0;
        formatTimeString = "00:00.00";
    }

    /**
     * 開始計時
     */
    public void startTimer(){
        if(handler == null){
            handler = new Handler();
        }

        if(watchRunnable == null){
            watchRunnable = new WatchRunnable();
        }

        isTimerRunning = true;
        startTime = SystemClock.elapsedRealtime();
        handler.post(watchRunnable);
    }

    /**
     * 暫停計時
     */
    public void pauseTimer(){
        isTimerRunning = false;
        offsetTime = elapsedTime;
        handler.removeCallbacks(watchRunnable);
    }

    /**
     * 結束計時
     */
    public void stopTimer(){
        pauseTimer();
        zeroWatch();
    }

    /**
     * 清除參數, 釋放資源
     */
    public void clear(){
        if(handler != null) {
            handler.removeCallbacks(watchRunnable);
        }

        handler = null;
        watchRunnable = null;
        mListener = null;
    }


    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    public String getFormatTimeString(){
        return formatTimeString;
    }


    /**
     * 計算時間所用
     */
    private class WatchRunnable implements Runnable{
        @Override
        public void run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime;
            elapsedTime += offsetTime;

            int milliSec = (int) (elapsedTime % 1000) / 10;
            int sec = (int) (elapsedTime / 1000) % 60;
            int min = (int) (elapsedTime / 1000) / 60;
            formatTimeString = String.format("%02d:%02d.%02d", min, sec, milliSec);

            //通知字串改變
            if(mListener != null){
                mListener.onFormatStringChanged(formatTimeString);
            }

            handler.postDelayed(this, 129);
        }
    }

    /**
     * 通知FormatTimeString的變化
     */
    public interface SimpleTimerListener{
        void onFormatStringChanged(String formatTime);
    }
}
