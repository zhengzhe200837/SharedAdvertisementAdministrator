package com.wind.main.retrofit.demo;

/**
 * Created by Administrator on 2017/12/5 0005.
 */

public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
