package com.wind.main.mode;

import android.graphics.Bitmap;

/**
 * Created by zhuyuqiang on 17-11-17.
 */

public class AdListItem {
    private Bitmap mIcon;
    private String mDetailAddr;
    private String mPhoneNumber;
    private String mAdDevice;
    private int mCount;

    public AdListItem(){};

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        this.mIcon = icon;
    }

    public String getDetailAddr() {
        return mDetailAddr;
    }

    public void setDetailAddr(String DetailAddr) {
        this.mDetailAddr = DetailAddr;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getAdDevice() {
        return mAdDevice;
    }

    public void setAdDevice(String adDevice) {
        this.mAdDevice = adDevice;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }
}
