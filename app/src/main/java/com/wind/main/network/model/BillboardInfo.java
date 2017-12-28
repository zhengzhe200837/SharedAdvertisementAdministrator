package com.wind.main.network.model;

import com.wind.main.util.LogUtil;

import java.io.Serializable;

/**
 * Created by zhengzhe on 2017/12/8.
 */

public class BillboardInfo implements Serializable{
    private String billboardId;
    private long price;
    private String address;
    private String equipmentType;
    private String screenType;
    private long screenWidth;
    private long screenHeight;
    private String startDate;
    private String  endDate;
    private String  startTime;
    private String endTime;
    private String pictureUrl="";
    private String equipmentAttribute;
    private String screenAttritute;
    private String businessPhone;
    private Double longitude;
    private Double latitude;
    private String tableName;
    private String todo;
    private String method;
    private String equipmentName;
    int orderCount ;

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getBillboardId() {
        return billboardId;
    }

    public void setBillboardId(String billboardId) {
        this.billboardId = billboardId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public long getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(long screenWidth) {
        this.screenWidth = screenWidth;
    }

    public long getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(long screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getEquipmentAttribute() {
        return equipmentAttribute;
    }

    public void setEquipmentAttribute(String equipmentAttribute) {
        this.equipmentAttribute = equipmentAttribute;
    }

    public String getScreenAttritute() {
        return screenAttritute;
    }

    public void setScreenAttritute(String screenAttritute) {
        this.screenAttritute = screenAttritute;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
