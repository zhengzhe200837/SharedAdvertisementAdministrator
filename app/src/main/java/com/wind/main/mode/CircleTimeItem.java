package com.wind.main.mode;

import com.wind.main.util.LogUtil;

/**
 * Created by zhuyuqiang on 17-11-24.
 */

public class CircleTimeItem {

    private String startTime;
    private String endTime;
    private int position;
    private long timeTamp = 0l;

    public long getTimeTamp() {
        return timeTamp;
    }

    public void setTimeTamp(long timeTamp) {
        LogUtil.e("zyq","time tamp = "+timeTamp);
        this.timeTamp = timeTamp;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
