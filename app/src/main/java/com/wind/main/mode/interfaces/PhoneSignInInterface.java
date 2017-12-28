package com.wind.main.mode.interfaces;

import com.wind.main.mode.results.SignInResult;
import com.wind.main.util.http.RequestParamsName;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by Administrator on 2017/12/4 0004.
 */

public interface PhoneSignInInterface {
    @FormUrlEncoded
    @POST("/NormalSignInServlet")
    Call<SignInResult> getRequestResult(@Field(RequestParamsName.SIGN_IN_PHONE_NUMBER) String name);
}
