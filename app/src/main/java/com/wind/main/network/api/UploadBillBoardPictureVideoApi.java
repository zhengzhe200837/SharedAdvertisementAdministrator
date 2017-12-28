package com.wind.main.network.api;
import com.wind.main.network.model.BillboardInfo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by xiaoxiao on 2017/12/13.
 */

public interface UploadBillBoardPictureVideoApi {
    @Multipart
    @POST ("/simpleDemo/UploadMediaSourceServlet")
    Observable<String> uploadPicture(@Part MultipartBody.Part pictureOrVidoeFile);

    Observable<BillboardInfo> getAdvertisementBoardDetailInfo(@Query("tableName") String tableName, @Query("todo") String todo);
}
