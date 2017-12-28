package com.wind.main.util;

import android.content.Context;

import org.afinal.simplecache.ACache;

/**
 * Created by xiaoxiao on 2017/12/26.
 */

public class UserLoginUtil {
   private static ACache aCache ;
    public  UserLoginUtil(Context context,boolean isLogin){
        aCache =ACache.get(context);
        aCache.put("isLogin",isLogin,30);
    }
}
