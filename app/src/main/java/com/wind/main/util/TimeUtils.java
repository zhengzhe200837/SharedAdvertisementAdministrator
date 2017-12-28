package com.wind.main.util;

/**
 * Created by zhuyuqiang on 17-11-19.
 */


import android.annotation.TargetApi;
//import android.icu.util.Calendar;
import java.util.Calendar;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具
 * Created by yangle on 2016/12/2.
 */
public class TimeUtils {

    public static String DATE_HOUR_MIN = "HH:mm";
    public static String DATE_MONTH_DAY = "MM/dd";
    public static long WEEK_TIME = 7*24*60*60*1000;
    public static long DAY_TIME = 24*60*60*1000;

    /**
     * 时间转换成字符串,指定格式
     *
     * @param time   时间
     * @param format 时间格式
     */
    public static String dateToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static String getCurrentDate(long time){
        Calendar calendar = Calendar.getInstance();
        int weekOfDay = calendar.get(Calendar.DAY_OF_WEEK);
        LogUtil.e("zyq","week of day = "+weekOfDay);
        if(weekOfDay < 2){
            time = time-(7-weekOfDay)*DAY_TIME;
        }else{
            time = time-(weekOfDay-2)*DAY_TIME;
        }
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static long getCurrentTimeOfWeek(long time){
        long t = 0l;
        Calendar calendar = Calendar.getInstance();
        int weekOfDay = calendar.get(Calendar.DAY_OF_WEEK);
        LogUtil.e("zyq1","week of day = "+weekOfDay);

        if(weekOfDay < 2){
            t = time-(7-weekOfDay)*DAY_TIME;
        }else{
            t = time-(weekOfDay-2)*DAY_TIME;
        }
        return t;
    }

    public static String dateToString(long time){
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static int getDayOfWeek(long time){
        Calendar calendar = Calendar.getInstance();
        int weekOfDay = calendar.get(Calendar.DAY_OF_WEEK);
        LogUtil.e("zyq","week of day = "+weekOfDay);

        if(weekOfDay < 2){
            weekOfDay = (7-weekOfDay);
        }else{
            weekOfDay = (weekOfDay-2);
        }
        return weekOfDay+1;
    }

    public static String getDateByFormat(long time,String format){
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

}
