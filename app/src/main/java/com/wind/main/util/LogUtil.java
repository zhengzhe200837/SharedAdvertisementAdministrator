package com.wind.main.util;

/**
 * Created by zhuyuqiang on 17-11-22.
 */

public class LogUtil {

    private LogUtil util = new LogUtil();

    public static boolean Debug = false;

    public static void e(String tag,String message){
        if(Debug){
            android.util.Log.e(tag,message);
        }
    }
}
