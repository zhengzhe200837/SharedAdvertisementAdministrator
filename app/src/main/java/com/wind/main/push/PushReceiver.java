package com.wind.main.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/7/2 0002.
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";

    //当受到消息，会回调onReceive()方法,onReceive中解析
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        //注册
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            String title = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i("minos", "title = " + title);
            //收到自定义消息
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //自定义消息可以发送json数据
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(extras);

                Log.i("minos", "from = " + jsonObject.get("from"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作(通知栏被点击)
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            //保存服务器推送下来的附加字段。这是个 JSON 字符串。
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            System.out.println("附加信息："+extras);
            try {
                JSONObject obj = new JSONObject(extras);
                String url = obj.getString("url");
                System.out.println("url："+url);
                //跳浏览器
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}

