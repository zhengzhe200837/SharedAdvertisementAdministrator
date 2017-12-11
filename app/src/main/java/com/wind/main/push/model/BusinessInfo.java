package com.wind.main.push.model;

/**
 * Created by zhengzhe on 2017/12/8.
 */

public class BusinessInfo {
    private long businessId;
    private String phoneNumber;

    public long getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(long id) {
        this.businessId = id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
