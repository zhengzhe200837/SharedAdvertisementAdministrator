package com.wind.main.retrofit.demo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class Result implements Serializable{
    private String result;
    private String logMessage;
    public Result(){}

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
