package com.larry.larrylibrary.util;


import androidx.annotation.Nullable;

import java.net.NetworkInterface;
import java.net.SocketException;

public class HardwareUtil {

    public static void listAllMacAddress(){
        String eth0 = getMacAddress("eth0");
        String eth1 = getMacAddress("eth1");
        String wlan0 = getMacAddress("wlan0");

        BaseLogUtil.logd("HardwareUtil", "eth0 = " + eth0 + ", eth1 = " + eth1 + ", wlan0 = " + wlan0);
    }

    @Nullable
    public static String getMacAddress(){
        String mac = getMacAddress("eth0");
        if(mac == null || mac.equals("")){
            mac = getMacAddress("eth1");
        }

        if(mac == null || mac.equals("")){
            mac = getMacAddress("wlan0");
        }

        return "".equals(mac) ? null : mac;
    }

    @Nullable
    public static String getMacAddress(String networkName){
        //获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，
        //不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，
        //这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。
//        String macAddress= "";
//        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        StringBuilder buf = new StringBuilder();
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(networkName);
            if (networkInterface == null) {
                return null;
            }

            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }

            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }
}
