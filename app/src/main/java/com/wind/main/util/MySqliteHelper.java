package com.wind.main.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xiaoxiao on 2017/12/22.
 */

public class MySqliteHelper extends SQLiteOpenHelper {
    private static final String name ="advertise";
    private static final int version =1;
    public MySqliteHelper(Context context){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists orderinfo(int id primary key ,orderId varchar(20) ,userPhone varchar(20), billBoardId varchar(20) ,playStartTime varchar(20),durationTime int,playTimes int,totalPrice int,orderStatus int,mediaName varchar(20),businessPhone varchar(20),currentTime varchar(20))");
//        db.execSQL("create table if not exists orderinfo(orderId varchar(20) primary key,userPhone varchar(20), billBoardId varchar(20) FOREIGN key,playStartTime varchar(20),durationTime integer,playTimes integer," +
//                "totalPrice long,orderStatus integer,mediaName varchar(20),businessPhone varchar(20),currentTime varchar(20))");
        db.execSQL("create table if not exists billboard(billboardId varchar(20) ,price int,address varchar(20),equipmentType varchar(20)," +
                "screenType varchar(20),screenWidth int,screenHeight int,startDate varchar(20),endDate varchar(20),startTime varchar(20),endTime varchar(20)," +
                "pictureUrl varchar(20),equipmentAttribute varchar(20),screenAttritute varchar(20),businessPhone varchar(20),longitude double,latitude double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
