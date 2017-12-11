package com.wind.main.mode.interfaces;


import com.wind.main.mode.results.SignInResult;
import com.wind.main.util.http.RequestParamsName;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.Call;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public interface NormalSignInInterface {
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_USER_NAME) String name,
                                        @Field(RequestParamsName.SIGN_IN_PASSWORD) String pwd);
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_USER_NAME) String name,
                                        @Field(RequestParamsName.SIGN_IN_PASSWORD) String pwd,
                                        @Field(RequestParamsName.SIGN_IN_PHONE_NUMBER) String phoneNumber);
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_USER_NAME) String name,
                                        @Field(RequestParamsName.SIGN_IN_PASSWORD) String pwd,
                                        @Field(RequestParamsName.SIGN_IN_PHONE_NUMBER) String phoneNumber,
                                        @Field(RequestParamsName.SIGN_IN_EMAIL) String email);
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_USER_NAME) String name,
                                        @Field(RequestParamsName.SIGN_IN_PASSWORD) String pwd,
                                        @Field(RequestParamsName.SIGN_IN_PHONE_NUMBER) String phoneNumber,
                                        @Field(RequestParamsName.SIGN_IN_EMAIL) String email,
                                        @Field(RequestParamsName.SIGN_IN_COUNTRY) String country);
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_USER_NAME) String name,
                                        @Field(RequestParamsName.SIGN_IN_PASSWORD) String pwd,
                                        @Field(RequestParamsName.SIGN_IN_PHONE_NUMBER) String phoneNumber,
                                        @Field(RequestParamsName.SIGN_IN_EMAIL) String email,
                                        @Field(RequestParamsName.SIGN_IN_COUNTRY) String country,
                                        @Field(RequestParamsName.SIGN_IN_CITY) String city);
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_USER_NAME) String name,
                                        @Field(RequestParamsName.SIGN_IN_PASSWORD) String pwd,
                                        @Field(RequestParamsName.SIGN_IN_PHONE_NUMBER) String phoneNumber,
                                        @Field(RequestParamsName.SIGN_IN_EMAIL) String email,
                                        @Field(RequestParamsName.SIGN_IN_COUNTRY) String country,
                                        @Field(RequestParamsName.SIGN_IN_CITY) String city,
                                        @Field(RequestParamsName.SIGN_IN_POSTCODE) String postcode);

}
