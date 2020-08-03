package com.larry.larrylibrary.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Larry on 2018/2/13.
 */

public class MD5Util {

    public static String getMD5(String toEncrypt){
        String result;

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            result = new BigInteger(1, md.digest()).toString(16);
        }catch (Exception e){
            e.printStackTrace();
            result = "";
        }

        return result;
    }
}
