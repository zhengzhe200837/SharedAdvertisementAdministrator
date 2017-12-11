package com.wind.main.mode;

/**
 * Created by zhuyuqiang on 17-11-19.
 */

public class PriceTimeItem {
    private String date;
    private String timeStart;
    private String timeStop;
    private int time_1_status;
    private int time_2_status;
    private int time_3_status;
    private int time_4_status;
    private int time_5_status;
    private int time_6_status;
    private int time_7_status;
    private int time_8_status;
    private int time_9_status;

    public int getTime_7_status() {
        return time_7_status;
    }

    public void setTime_7_status(int time_7_status) {
        this.time_7_status = time_7_status;
    }

    public int getTime_8_status() {
        return time_8_status;
    }

    public void setTime_8_status(int time_8_status) {
        this.time_8_status = time_8_status;
    }

    public int getTime_9_status() {
        return time_9_status;
    }

    public void setTime_9_status(int time_9_status) {
        this.time_9_status = time_9_status;
    }

    public int getTime_10_status() {
        return time_10_status;
    }

    public void setTime_10_status(int time_10_status) {
        this.time_10_status = time_10_status;
    }

    private int time_10_status;

    public int getTime_1_status() {
        return time_1_status;
    }

    public void setTime_1_status(int time_1_status) {
        this.time_1_status = time_1_status;
    }

    public int getTime_2_status() {
        return time_2_status;
    }

    public void setTime_2_status(int time_2_status) {
        this.time_2_status = time_2_status;
    }

    public int getTime_3_status() {
        return time_3_status;
    }

    public void setTime_3_status(int time_3_status) {
        this.time_3_status = time_3_status;
    }

    public int getTime_4_status() {
        return time_4_status;
    }

    public void setTime_4_status(int time_4_status) {
        this.time_4_status = time_4_status;
    }

    public int getTime_5_status() {
        return time_5_status;
    }

    public void setTime_5_status(int time_5_status) {
        this.time_5_status = time_5_status;
    }

    public int getTime_6_status() {
        return time_6_status;
    }

    public void setTime_6_status(int time_6_status) {
        this.time_6_status = time_6_status;
    }

    public PriceTimeItem(){};

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(String timeStop) {
        this.timeStop = timeStop;
    }

    public void setTimeStop(int hours,int minutes){
        String hour = null;
        String minute = null;
        if(hours <10){
            hour = "0"+String.valueOf(hours);
        }else{
            hour = String.valueOf(hours);
        }
        if(minutes < 10){
            minute = "0"+String.valueOf(minutes);
        }else{
            minute = String.valueOf(minutes);
        }
        setTimeStop(hour+":"+minute);
    }

    public void setTimeStart(int hours,int minutes){
        String hour = null;
        String minute = null;
        if(hours <10){
            hour = "0"+String.valueOf(hours);
        }else{
            hour = String.valueOf(hours);
        }
        if(minutes < 10){
            minute = "0"+String.valueOf(minutes);
        }else{
            minute = String.valueOf(minutes);
        }
        setTimeStart(hour+":"+minute);
    }

}
