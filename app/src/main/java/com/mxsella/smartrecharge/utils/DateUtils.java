package com.mxsella.smartrecharge.utils;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String stampToDate(long ms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Date date = new Date(ms);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

            return format.format(date);
        }

        Date date = new Date(ms);
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

        return simpleDateFormat.format(date);
    }
    public static String getDate2String(long j, String str) {
        return new SimpleDateFormat(str, Locale.getDefault()).format(new Date(j));
    }

    public static String getMinAndSecondBySeconds(long time) {
        int min = (int) (time / 60);
        int seconds = (int) (time % 60);
        return String.format(Locale.CHINA,"%02d", min) + " : " + String.format(Locale.CHINA,"%02d", seconds);
    }

    public static String getMinAndSecondByMilliSeconds(long time) {
        time = (int) Math.ceil(time / 1000f);
        int min = (int) (time / 60);
        int seconds = (int) (time % 60);
        return String.format(Locale.CHINA,"%02d", min) + ":" + String.format(Locale.CHINA,"%02d", seconds);
    }

    public static long getSeconds(String str) {
        String[] strs = str.replace(" ", "").split(":");
        int min = Integer.parseInt(strs[0]);
        int seconds = Integer.parseInt(strs[1]);
        return min * 60L + seconds;
    }

    public static String getNowDateTime() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(new Date());
        }
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-"
                + format(calendar.get(Calendar.MONTH)+1) + "-"//从0计算
                + format(calendar.get(Calendar.DAY_OF_MONTH)) + "-"
                + format(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + format(calendar.get(Calendar.MINUTE)) + ":"
                + format(calendar.get(Calendar.SECOND));
    }

    /**
     * 时间装换为毫秒
     */
    public static long timeToMs(int n) {
        return (long) n * 24 * 60 * 60 * 1000;
    }

    public static String duration(int s1, int s2, int times) {
        int seconds = times * (s1 + s2);
        int min = seconds / 60;
        int s = seconds % 60;
        return min + "min" + s + "s";
    }

    private static String format(int time) {
        return String.format(Locale.getDefault(),"%02d", time);
    }
    public static String formatDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
