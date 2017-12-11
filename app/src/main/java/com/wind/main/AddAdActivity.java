package com.wind.main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.main.util.LogUtil;

import java.io.File;

/**
 * Created by zhuyuqiang on 17-11-18.
 */

public class AddAdActivity extends AppCompatActivity {

    private Button mAddAdEnsure,mAddAdCancel;
    private ImageView mAddAdImage;
    private TextView mAddAdUploadFile,mAddAdRenameFile,mAddAdDeleteFile;
    private TextView mAddAdValidTime,mAddAdDetailAddr,mAddAdDeviceName,mAddAdPhoneNumebr;
    private String mImagePath;
    private String mImageFile;
    private final int REQUEST_PICTURE = 0;
    private final int UPDATE_PICTURE = 1;
    private final int UPDATE_FILE_NAME = 2;
    private final int PICTURE_WIDTH = 200;
    private final int PICTURE_HEIGHT = 200;
    private AlertDialog mModifyName = null;
    private AlertDialog mDetailAddr = null;
    private AlertDialog mDeviceName = null;
    private AlertDialog mPhoneName = null;
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_PICTURE:
//                    mAddAdImage.setImageBitmap(getBitmapByPath());
                    mAddAdImage.setImageBitmap(getLocalVideoThumbnail(mImagePath));
                    break;
                case UPDATE_FILE_NAME:
                    File file = new File(mImagePath);
                    mImageFile = file.getName();
                    mAddAdUploadFile.setText(mImageFile);
                    break;
            }

        }
    };

    private Bitmap getBitmapByPath(){
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImagePath,op);
        if(op.outHeight>PICTURE_HEIGHT || op.outWidth > PICTURE_WIDTH){
            op.inSampleSize = Math.max(op.outHeight/PICTURE_HEIGHT,op.outWidth/PICTURE_WIDTH);
            op.inJustDecodeBounds = false;
        }
        return BitmapFactory.decodeFile(mImagePath,op);
    }

    public Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = scaleBitmap(retriever.getFrameAtTime());

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    private Bitmap scaleBitmap(Bitmap bm){
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = 200;
        int newHeight = 200;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(com.wind.main.R.style.add_ad_Theme);
        setContentView(com.wind.main.R.layout.add_ad_layout);
        initView();
    }

    private void initView(){
        mAddAdEnsure = (Button) findViewById(com.wind.main.R.id.add_ad_ensure);
        mAddAdCancel = (Button) findViewById(com.wind.main.R.id.add_ad_cancel);
        mAddAdImage = (ImageView) findViewById(com.wind.main.R.id.add_ad_image_view);
        mAddAdUploadFile = (TextView) findViewById(com.wind.main.R.id.add_ad_upload_file);
        mAddAdRenameFile = (TextView) findViewById(com.wind.main.R.id.add_ad_rename_file);
        mAddAdDeleteFile = (TextView) findViewById(com.wind.main.R.id.add_ad_delete_file);
        mAddAdValidTime = (TextView) findViewById(com.wind.main.R.id.add_ad_valid_time);
        mAddAdDetailAddr = (TextView) findViewById(com.wind.main.R.id.add_ad_detail_addr);
        mAddAdDeviceName = (TextView) findViewById(com.wind.main.R.id.add_ad_device_name);
        mAddAdPhoneNumebr = (TextView) findViewById(com.wind.main.R.id.add_ad_phone_number);
    }

    private void getPicFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_PICTURE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mImagePath = cursor.getString(columnIndex);
            cursor.close();
            LogUtil.e("zyq","ImagePath = "+mImagePath);
            mUIHandler.sendEmptyMessage(UPDATE_PICTURE);
            mUIHandler.sendEmptyMessage(UPDATE_FILE_NAME);
        }
    }

    private void applyListener(){
        if(mAddAdEnsure != null){
            mAddAdEnsure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddAdActivity.this, MainActivity.class);
                    Bundle data = new Bundle();
                    data.putString("image_path",mImagePath);
                    data.putBoolean("is_login",true);
                    data.putString("detail_addr",mAddAdDetailAddr.getText().toString());
                    data.putString("device_name",mAddAdDeviceName.getText().toString());
                    data.putString("phone_number",mAddAdPhoneNumebr.getText().toString());
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();
                }
            });
        }
        if(mAddAdCancel != null){
            mAddAdCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddAdActivity.this,MainActivity.class);
                    intent.putExtra("is_login",true);
                    startActivity(intent);
                    finish();
                }
            });
        }
        if(mAddAdImage != null){
            mAddAdImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPicFromGallery();
                }
            });
        }

        if(mAddAdUploadFile != null){
            mAddAdUploadFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPicFromGallery();
                }
            });
        }

        if (mAddAdRenameFile != null){
            mAddAdRenameFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!"".equals(mAddAdUploadFile.getText())){
                        modifyImageName();
                    }
                }
            });
        }

        if(mAddAdDeleteFile != null){
            mAddAdDeleteFile.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mImagePath = "";
                    mAddAdImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), com.wind.main.R.drawable.my_video));
                    mAddAdUploadFile.setText("");
                    mAddAdUploadFile.setHint(com.wind.main.R.string.add_ad_upload_file);
                }
            });
        }
        if(mAddAdDetailAddr != null){
            mAddAdDetailAddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputDetailAddr();
                }
            });
        }

        if(mAddAdDeviceName != null){
            mAddAdDeviceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputDeviceName();
                }
            });
        }

        if(mAddAdPhoneNumebr != null){
            mAddAdPhoneNumebr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputPhoneNumber();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
    }

    private void cancelListener(){
        if(mAddAdEnsure != null){
            mAddAdCancel.setOnClickListener(null);
        }
        if(mAddAdCancel != null){
            mAddAdCancel.setOnClickListener(null);
        }
        if(mAddAdImage != null){
            mAddAdImage.setOnClickListener(null);
        }
        if(mAddAdUploadFile != null){
            mAddAdUploadFile.setOnClickListener(null);
        }
        if (mAddAdRenameFile != null){
            mAddAdRenameFile.setOnClickListener(null);
        }
        if(mAddAdDeleteFile != null){
            mAddAdDeleteFile.setOnClickListener(null);
        }
        if(mAddAdDetailAddr != null){
            mAddAdDetailAddr.setOnClickListener(null);
        }

        if(mAddAdDeviceName != null){
            mAddAdDeviceName.setOnClickListener(null);
        }

        if(mAddAdPhoneNumebr != null){
            mAddAdPhoneNumebr.setOnClickListener(null);
        }
    }

    private void modifyImageName(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
        title.setText("修改名称");
        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mModifyName != null){
                    mModifyName.dismiss();
                }
            }
        });
        Button ensure = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mModifyName != null){
                    mImageFile = editor.getText().toString();
                    if(mImageFile != null && !"".equals(mImageFile)){
                        mAddAdUploadFile.setText(mImageFile);
                        mModifyName.dismiss();
                    }
                }
            }
        });
        mBuilder.setView(view);
        mBuilder.setCancelable(true);
        mModifyName = mBuilder.create();
        mModifyName.show();
    }
    private void inputDetailAddr(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
        title.setText("详细地址");
        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDetailAddr != null){
                    mDetailAddr.dismiss();
                }
            }
        });
        Button ensure = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDetailAddr != null){
                    String detailAddr = editor.getText().toString();
                    if(!TextUtils.isEmpty(detailAddr)){
                        mAddAdDetailAddr.setText(detailAddr);
                        mDetailAddr.dismiss();
                    }
                }
            }
        });
        mBuilder.setView(view);
        mBuilder.setCancelable(true);
        mDetailAddr = mBuilder.create();
        mDetailAddr.show();
    }

    private void inputDeviceName(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
        title.setText("设备名称");
        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDeviceName != null){
                    mDeviceName.dismiss();
                }
            }
        });
        Button ensure = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDeviceName != null){
                    String detailAddr = editor.getText().toString();
                    if(detailAddr != null && !"".equals(detailAddr)){
                        mAddAdDeviceName.setText(detailAddr);
                        mDeviceName.dismiss();
                    }
                }
            }
        });
        mBuilder.setView(view);
        mBuilder.setCancelable(true);
        mDeviceName = mBuilder.create();
        mDeviceName.show();
    }

    private void inputPhoneNumber(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
        title.setText("联系电话");
        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhoneName != null){
                    mPhoneName.dismiss();
                }
            }
        });
        Button ensure = (Button)view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhoneName != null){
                    String detailAddr = editor.getText().toString();
                    if(detailAddr != null && !"".equals(detailAddr)){
                        mAddAdPhoneNumebr.setText(detailAddr);
                        mPhoneName.dismiss();
                    }
                }
            }
        });
        mBuilder.setView(view);
        mBuilder.setCancelable(true);
        mPhoneName = mBuilder.create();
        mPhoneName.show();
    }

}
