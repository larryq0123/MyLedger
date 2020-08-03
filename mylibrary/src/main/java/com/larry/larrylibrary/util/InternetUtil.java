package com.larry.larrylibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
    }

    public static void checkInternetConnection(Consumer consumer){
        new InternetCheck(consumer).execute();
    }

    private static class InternetCheck extends AsyncTask<Void,Void,Boolean> {
        private Consumer mConsumer;

        InternetCheck(Consumer consumer) { mConsumer = consumer; }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Socket sock = new Socket();
            try {
                // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
                //Google DNS (e.g. 8.8.8.8) is the largest public DNS in the world. As of 2013 it served 130 billion requests a day.
                sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                sock.close();
                return true;
            } catch (IOException e) {
                return false;
            }finally {
                if(!sock.isClosed()){
                    try{
                        sock.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean hasInternet) {
            mConsumer.accept(hasInternet);
        }
    }

    public interface Consumer { void accept(Boolean hasInternet); }
}
