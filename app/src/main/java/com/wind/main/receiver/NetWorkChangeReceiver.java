package com.wind.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wind.main.util.LogUtil;

/**
 * Created by zhuyuqiang on 17-11-22.
 */

public class NetWorkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("zyq","NetWorkChangeReceiver : onReceive");
    }
}
