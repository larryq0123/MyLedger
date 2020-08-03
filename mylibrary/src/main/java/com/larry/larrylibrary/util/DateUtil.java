package com.larry.larrylibrary.util;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static String getStringPlace(int num, int place) {
        NumberFormat nf = NumberFormat.getInstance();

        nf.setGroupingUsed(false); //取消逗號
        nf.setMinimumIntegerDigits(place);
        return nf.format(num);
    }

    public static SimpleDateFormat getSimpleDateTimeFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        String date = year + "/" + getStringPlace(month, 2) + "/" + getStringPlace(day, 2) + " " +
                getStringPlace(hour, 2) + ":" + getStringPlace(minute, 2) + ":" + getStringPlace(second, 2);

        return date;
    }

    public static String getDateTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        return year + "" + getStringPlace(month, 2) + "" + getStringPlace(day, 2) + "_" +
                getStringPlace(hour, 2) + "" + getStringPlace(minute, 2) + "" + getStringPlace(second, 2);
    }

    public static String bigIntegerToDateString(BigInteger time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time.longValue());
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return dateSdf.format(c.getTime());
    }


    public static int differenceBetweenYears(String dateString, SimpleDateFormat dateFormat) {
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        try {
            beforeCalendar.setTime(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int differenceYears = (int) ((nowCalendar.getTimeInMillis() - beforeCalendar.getTimeInMillis()) / (1000 * 24 * 3600) / 365);

        return differenceYears;
    }
}
