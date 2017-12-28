package com.wind.main.network.api;

import com.google.gson.Gson;
import com.wind.main.network.model.BillboardAndOrder;
import com.wind.main.network.model.BillboardInfo;
import com.wind.main.network.model.OrderInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by zhengzhe on 2017/12/11.
 */

public interface UploadBillBoardInfoApi {
    @POST("/simpleDemo/HandleDataBaseServlet")
    Observable<String> upload(@Body BillboardInfo billboardInfo);
    @Multipart
    @POST ("/simpleDemo/HandleDataBaseServlet")
    Observable<String> uploadPicture(@Part MultipartBody.Part pictureOrVidoeFile);
    @POST("/simpleDemo/HandleDataBaseServlet")
    Observable<List<BillboardInfo>> selectBillBoardInfo(@Query("tableName") String tableName, @Query("todo") String todo);
    @GET("/simpleDemo/HandleDataBaseServlet")
    Observable<BillboardInfo> getBillBoardInfo(@Query("tableName")String tableName ,@Query("todo")String todo);
    @POST("/simpleDemo/HandleDataBaseServlet")
    Observable<List<BillboardInfo>> getBillBoardInfo(@Body BillboardInfo billboardInfo);
    @POST("/simpleDemo/HandleDataBaseServlet")
    Observable<List<OrderInfo>> getOrderInfo(@Body OrderInfo orderInfo);
    @POST("/simpleDemo/HandleDataBaseServlet")
    Observable<Object> updateOrderInfo(@Body OrderInfo orderInfo);
}
