package com.wind.main.retrofit.demo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import retrofit.Call;
//import retrofit.Callback;
//import retrofit.GsonConverterFactory;

//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.ResponseBody;

//import retrofit.Response;
//import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private String mImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getVideoPath(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mImagePath = cursor.getString(columnIndex);
            cursor.close();
        }
    }

//    public void sendRequest(View view){
//        Log.e("zyq","sendRequest");
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.31.62:8090/WindSD/")
////                .addConverterFactory(GsonConverterFactory.create(new Gson()))
//                .build();
//        retrofit.create(TestInterface.class).getString("_hello","123456").enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Response<Result> response, Retrofit retrofit) {
//                Log.e("zyq","onResponse");
//                Log.e("zyq","message="+response.message());
//                Result r = response.body();
//                Log.e("zyq","r == null:"+(r == null));
//                if(r != null){
//                    Log.e("zyq","r="+r.getResult()+"...: "+r.getLogMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e("zyq","onFailure = " +t.getMessage());
//            }
//        });
//    }

    public void uploadFile(View view){
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("http://192.168.31.62:8090/WindSD/")
//                .client(new OkHttpClient())
//                .build();
//        UploadFileInterface service = retrofit.create(UploadFileInterface.class);
//        File file = new File("/sdcard/Pictures/zhuyuqiang.jpg");
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
//
//        String descriptionString = "This is a description";
//        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
//
//        Call<ResponseBody> call = service.upload(description, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                Log.e("zyq","string  = "+response.message());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e("zyq","onFailure message = "+t.getMessage());
//            }
//        });
        Log.e("zyq","start uploadFile");
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        OkHttpClient client = builder.build();
        File file = new File("/sdcard/Pictures/zhuyuqiang.jpg");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
        } else {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            final RequestBody requestBody = new MultipartBody.Builder().addFormDataPart("filename", file.getName(), fileBody).build();

            Request requestPostFile = new Request.Builder()
                    .url("http://192.168.31.62:8090/WindSD/UploadFileServlet")
                    .post(requestBody)
                    .build();
            client.newCall(requestPostFile).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("zyq","onFailure :message = "+e.getMessage());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.e("zyq","onResponse :message = "+response.message());
                }
            });
        }
    }

    private ProgressResponseListener progressListener = new ProgressResponseListener() {
        @Override
        public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
            Log.e("zyq","bytesRead = "+bytesRead+",contentLength = "+contentLength);
        }
    };

}
