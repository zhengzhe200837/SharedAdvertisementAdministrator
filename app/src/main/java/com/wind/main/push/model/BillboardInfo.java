package com.wind.main.push.model;

/**
 * Created by zhengzhe on 2017/12/8.
 */

public class BillboardInfo {
    private long billboardId;
    private long price;
    private String address;
    private long businessId;
    private String equipmentType;
    private String screenType;
    private long screenWidth;
    private long screenHeight;

    public void setBillBoardInfo(long price, String address, long businessId,String equipmentType,
                                 String screenType, long screenWidth, long screenHeight){
        this.price = price;
        this.address = address;
        this.businessId = businessId;
        this.equipmentType = equipmentType;
        this.screenType = screenType;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public long getBillboardId() {
        return this.billboardId;
    }

    public void setBillboardId(long id) {
        this.billboardId = id;
    }

    public long getBusinessId() {
        return this.businessId;
    }

    public void setbusinessId(long id) {
        this.businessId = id;
    }

    public long getPrice() {
        return this.price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEquipmentType() {
        return this.equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getScreenType() {
        return this.screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public long getScreenWidth() {
        return this.screenWidth;
    }

    public void setScreenWidth(long width) {
        this.screenWidth = width;
    }

    public long getScreenHeight() {
        return this.screenHeight;
    }

    public void setScreenHeight(long height) {
        this.screenHeight = height;
    }

//	@Override
//	public String toString() {
//		return String.format("id:%d,name:%s,description:%s", id, name, description);
//	}
}
