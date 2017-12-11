package com.wind.main.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wind.main.util.EnumUtil;

/**
 * Created by zhuyuqiang on 17-11-22.
 */

public class NetWorkManager {
    private static ConnectivityManager mConnectivityManager;
    private NetWorkManager mNetWorkManager = new NetWorkManager();
    private NetWorkManager(){

    }

    public static void initConnectivityManager(Context context){
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isValidNetWork(Context context){
        if(mConnectivityManager == null){
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        int status = 0;
        //wifi
        NetworkInfo.State state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            status = EnumUtil.NETWORK_WIFI;
        }

        //mobile
        state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            status = EnumUtil.NETWORK_MOBILE;
        }

        return status != EnumUtil.NETWORK_NONE;
    }

}
