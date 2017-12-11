package com.wind.main.retrofit.demo;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public interface TestInterface {
    @FormUrlEncoded
    @POST("LogInServlet")
    Call<Result> getString(@Field("phone_number") String name,
                           @Field("password")String pwd);

}
