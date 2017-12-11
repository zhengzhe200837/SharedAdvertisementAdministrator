package com.wind.main;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.wind.main.manager.NetWorkManager;
import com.wind.main.util.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhuyuqiang on 17-11-22.
 */

public class SDApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        NetWorkManager.initConnectivityManager(getSDBaseContext());
        LogUtil.Debug = isApkInDebug(mContext);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }

    public boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
    }

    public static Context getSDBaseContext() {
        return mContext;
    }

}
