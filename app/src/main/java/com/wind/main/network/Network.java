package com.wind.main.network;


import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.wind.main.network.api.UploadBillBoardInfoApi;
import com.wind.main.network.api.UploadBillBoardPictureVideoApi;
import com.wind.main.network.model.BillboardInfo;
import com.wind.main.network.model.OrderInfo;
import com.wind.main.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zhengzhe on 2017/12/11.
 */

public class Network {
    private static UploadBillBoardInfoApi mUploadBillBoardInfoApi = null;
    private static UploadBillBoardPictureVideoApi mUploadBillBoardPictureVideoApi = null;
    private static Converter.Factory mGsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory mRxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    //add
    static List<BillboardInfo> billboardInfos;

    public static void uploadBillBoardInfo(BillboardInfo bi) {
        getUploadBillBoardInfoApi();
        Gson gson = new Gson();
        LogUtil.d(gson.toJson(bi));
        mUploadBillBoardInfoApi.upload(bi)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        android.util.Log.d("zz", "Network + uploadVideoFile + s = " + s);
                        if ("success".equals(s)) {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        android.util.Log.d("zz", "Network + uploadVideoFile + throwable = " + throwable.toString());
                    }
                });
    }
    public static File createObjFile(File file,String id,String businessPhone){
        File file1 =new File(file.getParent()+"/"+id+"_"+businessPhone +"_"+file.getName());
        int len =-1;
        FileInputStream fis =null;
        FileOutputStream fos =null;
        try {
            byte[] bytes =new byte[1024];
             fis =new FileInputStream(file);
             fos = new FileOutputStream(file1);
            while((len =fis.read(bytes))!=-1){
                fos.write(bytes,0,len);
            }
        }catch (FileNotFoundException fnf){
            return null;
        }catch (IOException IO){
            return null;
        }finally {
            try {
                fos.close();
                fis.close();

            }catch (IOException io){
                return null;
            }
        }return file1;
    }

    public static UploadBillBoardInfoApi getUploadBillBoardInfoApi() {
        if (mUploadBillBoardInfoApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                   .baseUrl("http://111.231.110.234:8080")// .baseUrl("http://192.168.31.233:8080")
                    .client(mOkHttpClient)
                    .addConverterFactory(mGsonConverterFactory)
                    .addCallAdapterFactory(mRxJavaCallAdapterFactory)
                    .build();
            mUploadBillBoardInfoApi = retrofit.create(UploadBillBoardInfoApi.class);
        }
        return mUploadBillBoardInfoApi;
    }

    public static void UploadPictureOrVideoApi(final Context context, File file,String id,String bussinessPhone) {
        String name =file.getName();
//        LogUtil.d("name: "+name +"" +"path"+file.getPath() +"parent: "+file.getParent() +"id"+id +"busphone: "+bussinessPhone);
//        File file1 =new File(file.getParent(),"/"+id+"_"+bussinessPhone+"_"+name);
//        LogUtil.d("file1: "+file1.getPath());
//        file.renameTo(file1);
        File file1 =createObjFile(file,id,bussinessPhone);
        LogUtil.d("name: "+name +"" +"path"+file1.getPath() );
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file1);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file1.getName(),
                requestFile);
        getUploadPictureOrVideoApi().uploadPicture(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                LogUtil.d("accept: "+s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                LogUtil.d("throwable: "+throwable.getMessage());
            }
        });
    }

    static BillboardInfo billboardInfo;
    static Object object = new Object();
    static Lock lock = new ReentrantLock();

    public  static BillboardInfo queryBillboardInfo(String tableName, String action) {
        if (mUploadBillBoardInfoApi == null) {
            mUploadBillBoardInfoApi = getUploadBillBoardInfoApi();
        }

        mUploadBillBoardInfoApi.getBillBoardInfo(tableName, action)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread())
                .subscribe(new Consumer<BillboardInfo>() {
                    @Override
                    public void accept(@NonNull BillboardInfo billboardInfo1) throws Exception {
                        LogUtil.d("billboard1: " + billboardInfo1.getAddress());
                        billboardInfo = billboardInfo1;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtil.d("throwable: " + throwable.getMessage());
                    }
                });

        while(billboardInfo == null);
        LogUtil.d("billinfo1: " + billboardInfo);
//        while(billboardInfo == null);
        return billboardInfo;
    }


    static List<OrderInfo> orderInfos;
//    public static List<OrderInfo> getOrderInfo(String tableName,String todo){
//        orderInfos =new ArrayList<>();
//        if(mUploadBillBoardInfoApi==null){
//            mUploadBillBoardInfoApi = getUploadBillBoardInfoApi();
//        }
//        mUploadBillBoardInfoApi.getOrderInfo(tableName, todo).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<OrderInfo>>() {
//            @Override
//            public void accept(@NonNull List<OrderInfo> orderInfos1) throws Exception {
//               orderInfos =orderInfos1;
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                LogUtil.d("throwable: "+throwable.getMessage());
//            }
//        });
//         while (orderInfos ==null);
//         return orderInfos;
//    }

    private static UploadBillBoardPictureVideoApi getUploadPictureOrVideoApi() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setLenient().create();
        if (mUploadBillBoardPictureVideoApi == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://111.231.110.234:8080")//baseUrl("http://192.168.31.233:8080")
                    .client(mOkHttpClient).addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(mRxJavaCallAdapterFactory).build();
            mUploadBillBoardPictureVideoApi = retrofit.create(UploadBillBoardPictureVideoApi.class);
        }
        return mUploadBillBoardPictureVideoApi;

    }
    public static void updateOrderInfo(final Context context,OrderInfo orderInfo){
        if (mUploadBillBoardInfoApi==null){
            mUploadBillBoardInfoApi =getUploadBillBoardInfoApi();
        }
        mUploadBillBoardInfoApi.updateOrderInfo(orderInfo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object object) throws Exception {
                        Toast.makeText(context, object.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
