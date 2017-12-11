package com.wind.main.retrofit.demo;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import okhttp3.MultipartBody;
import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by Administrator on 2017/12/5 0005.
 */

public interface UploadFileInterface {
    @Multipart
    @POST("UploadFileServlet")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part("filekey") MultipartBody.Part file);
}
