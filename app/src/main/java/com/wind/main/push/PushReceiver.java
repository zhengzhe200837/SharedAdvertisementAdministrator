package com.wind.main.push;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.wind.main.OrderFormActivity;
import com.wind.main.network.model.OrderInfo;
import com.wind.main.util.LogUtil;
import com.wind.main.util.MySqliteHelper;
import com.wind.main.util.OrderInfoUtil;

import org.afinal.simplecache.ACache;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;

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
            ACache aCache =ACache.get(context);
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            aCache.put("orderinfo",extras);
            LogUtil.d("extras: "+extras);
            try {
                MySqliteHelper mySqliteHelper =new MySqliteHelper(context);
                JSONObject jsonObject = new JSONObject(extras);
                String orderId =jsonObject.getString("orderId");
                String userPhone =jsonObject.getString("userPhone");
                String billBoardId =jsonObject.getString("billBoardId");
                String playStartTime =jsonObject.getString("playStartTime");
                int durationTime =jsonObject.getInt("durationTime");
                int playTimes =jsonObject.getInt("playTimes");
                long totalPrice =jsonObject.getLong("totalPrice");
                int orderStatus =jsonObject.getInt("orderStatus");
                String mediaName =jsonObject.getString("mediaName");
                String businessPhone =jsonObject.getString("businessPhone");
//                OrderInfo orderInfo =new OrderInfo();
//                orderInfo.setOrderId(orderId);
//                orderInfo.setUserPhone(userPhone);
//                orderInfo.setBillboardId(billBoardId);
//                orderInfo.setPlayStartTime(playStartTime);
//                orderInfo.setDurationTime(durationTime);
//                orderInfo.setPlayTimes(playTimes);
//                orderInfo.setTotalPrice(totalPrice);
//                orderInfo.setOrderStatus(orderStatus);
//                orderInfo.setMediaName(mediaName);
//                orderInfo.setBusinessPhone(businessPhone);
//                 aCache.put("orderInfo",orderInfo);
//                LogUtil.d("orderInf: "+orderInfo.getOrderId());
                SQLiteDatabase sqLiteDatabase =
                mySqliteHelper.getWritableDatabase();
                ContentValues contentValues =new ContentValues();
                contentValues.put("orderId",orderId);
                contentValues.put("userPhone",userPhone);
                contentValues.put("billBoardId",billBoardId);
                contentValues.put("playStartTime",playStartTime);
                contentValues.put("durationTime",durationTime);
                contentValues.put("playTimes",playTimes);
                contentValues.put("totalPrice",totalPrice);
                contentValues.put("orderStatus",orderStatus);
                contentValues.put("mediaName",mediaName);
                contentValues.put("businessPhone",businessPhone);
                Calendar calendar =Calendar.getInstance();
                int year =calendar.get(Calendar.YEAR);
                int month =calendar.get(Calendar.MONTH);
                int day =calendar.get(Calendar.DAY_OF_MONTH);
                contentValues.put("currentTime", year+""+month+""+day);
                sqLiteDatabase.insert("orderinfo",null,contentValues);
                sqLiteDatabase.close();
                Log.i("minos", "from = " + jsonObject.get("from"));
//                OrderInfoUtil.setOrders(orderInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作(通知栏被点击)
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d("ACTION_NOTIFICATION_OPENED");
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            //保存服务器推送下来的附加字段。这是个 JSON 字符串。
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogUtil.d(extras);
            System.out.println("附加信息："+extras);
            try {
                JSONObject obj = new JSONObject(extras);
                ACache aCache =ACache.get(context);
                 JSONObject jsonObject = aCache.getAsJSONObject("orderinfo");
                String aa =jsonObject.getString("message");
                LogUtil.d("aa: "+aa);
                 jsonObject = new JSONObject(aa);
                LogUtil.d(jsonObject+"");
//                Iterator iterator = jsonObject.keys();
//                while (iterator.hasNext()){
//                    String key =(String) iterator.next();
//                    String value =(String) jsonObject.get(key);
//                    LogUtil.d("key: "+key +"value: "+value);
//
//                }
               // String url = obj.getString("url");order_status media_name
              //  System.out.println("url："+url);
                Intent orderShow =new Intent();
                LogUtil.d("order_id: "+jsonObject.getString("orderId") +" order_status: "+jsonObject.getInt("orderStatus") +" media_name: "+jsonObject.getString("mediaName"));
                orderShow.putExtra("order_id",jsonObject.getString("orderId"));
                orderShow.putExtra("order_status",jsonObject.getInt("orderStatus"));
                orderShow.putExtra("media_name",jsonObject.getString("mediaName"));
                orderShow.setClass(context, OrderFormActivity.class);
                context.startActivity(orderShow);
                //跳浏览器
            } catch (JSONException e) {
                LogUtil.d("jsonException: "+e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }


}

