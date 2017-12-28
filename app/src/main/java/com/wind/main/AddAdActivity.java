package com.wind.main;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wind.main.dialog.TimePickerDialog;
import com.wind.main.network.Network;
import com.wind.main.network.model.BillboardInfo;
import com.wind.main.util.LogUtil;
import com.wind.main.util.MySqliteHelper;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhuyuqiang on 17-11-18.
 */

public class AddAdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private SharedPreferences sp;
    private Context mContext;
    private Button mAddAdEnsure,mAddAdCancel;
    private ImageView mAddAdImage;
    private TextView mAddAdUploadFile,mAddAdRenameFile,mAddAdDeleteFile;
    private TextView mAddAdValidTime,mAddAdDetailAddr,mAddAdDeviceName,mAddAdPhoneNumebr;
    private String mImagePath;
    private String mImageFile;
    private final int REQUEST_PICTURE = 0;
    private final int REQUEST_ADD_AD =1;
    private final int UPDATE_PICTURE = 1;
    private final int UPDATE_FILE_NAME = 2;
    private final int PICTURE_WIDTH = 200;
    private final int PICTURE_HEIGHT = 200;
    private AlertDialog mModifyName = null;
    private AlertDialog mDetailAddr = null;
    private AlertDialog mDeviceName = null;
    private AlertDialog mPhoneName = null;
    private TextView mAddOpenTime;
    private ListView mAdItems;
    private AdAdapter adAdapter;
    private List<BillboardInfo> billboardInfos;
    private static int id =0;
    private static List<String> items =new ArrayList<>();;
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_PICTURE:
                    mAddAdImage.setImageBitmap(getBitmapByPath());
//                    mAddAdImage.setImageBitmap(getLocalVideoThumbnail(mImagePath));
                    break;
                case UPDATE_FILE_NAME:
                    File file = new File(mImagePath);
                    mImageFile = file.getName();
                    mAddAdUploadFile.setText(mImageFile);
                    break;
            }

        }
    };
    private TimePickerDialog timePickerDialog;
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

    /**
     * 获取指定路径下视频的缩略图
     */
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
        mContext =this;
        initView();
    }

    public void onResume(){
        super.onResume();
        if(mBillboardInfo ==null){
            mAddAdEnsure.setVisibility(View.INVISIBLE);
        }
    }

    private void initView(){
        mAddAdEnsure = (Button) findViewById(com.wind.main.R.id.add_ad_ensure);
        mAddAdCancel = (Button) findViewById(com.wind.main.R.id.add_ad_cancel);
        mAddAdImage = (ImageView) findViewById(com.wind.main.R.id.add_ad_image_view);
        mAddAdUploadFile = (TextView) findViewById(com.wind.main.R.id.add_ad_upload_file);
        mAddAdRenameFile = (TextView) findViewById(com.wind.main.R.id.add_ad_rename_file);
        mAddAdDeleteFile = (TextView) findViewById(com.wind.main.R.id.add_ad_delete_file);
        mAddOpenTime =(TextView)findViewById(R.id.add_open_time);
        mAdItems =(ListView)findViewById(R.id.list_open_time);
        billboardInfos =new ArrayList<>();
        adAdapter =new AdAdapter();

        mAdItems.setAdapter(adAdapter);
        sp =getSharedPreferences("billboardId",Context.MODE_PRIVATE);

//        mAddAdValidTime = (TextView) findViewById(com.wind.main.R.id.add_ad_valid_time);
//        mAddAdDetailAddr = (TextView) findViewById(com.wind.main.R.id.add_ad_detail_addr);
//        mAddAdDeviceName = (TextView) findViewById(com.wind.main.R.id.add_ad_device_name);
//        mAddAdPhoneNumebr = (TextView) findViewById(com.wind.main.R.id.add_ad_phone_number);
    }

    private void getPicFromGallery(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");
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
            LogUtil.d("selectedImage: "+selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                mImagePath = cursor.getString(columnIndex);
                cursor.close();
                LogUtil.e("zyq", "ImagePath = " + mImagePath);
                mUIHandler.sendEmptyMessage(UPDATE_PICTURE);
                mUIHandler.sendEmptyMessage(UPDATE_FILE_NAME);
        }else if(requestCode ==REQUEST_ADD_AD && resultCode ==RESULT_OK){
            LogUtil.d("hahaha");
            if(data !=null) {
                LogUtil.d("data: "+data.getStringExtra("item_name"));
                mBillboardInfo =(BillboardInfo)data.getSerializableExtra("billboardinfo");
                LogUtil.d("latitude: "+mBillboardInfo.getLatitude() +" longitude: "+mBillboardInfo.getLongitude());
                if(mBillboardInfo !=null){
                 mAddAdEnsure.setVisibility(View.VISIBLE);
                }
                mBillboardInfo.setPictureUrl(mAddAdUploadFile.getText().toString());
                items.add(data.getStringExtra("item_name"));
                adAdapter.notifyDataSetChanged();
            }
          //  finish();
        }
    }

    private BillboardInfo mBillboardInfo;
    private void createBillBoardInfo() {
        mBillboardInfo = new BillboardInfo();
        mBillboardInfo.setAddress(mAddAdDetailAddr.getText().toString());
        mBillboardInfo.setBusinessPhone(mAddAdPhoneNumebr.getText().toString());
        mBillboardInfo.setPrice(15);
        mBillboardInfo.setTableName("billBoardInfo");

    }

    public String getWifiMacAddress(){
        //b438471fd93325d2 70e5731b926c6a82
        LogUtil.d(":"+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    private void applyListener(){
        if(mAddAdEnsure != null){
            mAddAdEnsure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int billboardid =sp.getInt("billboardid",10);
                    LogUtil.d("billboardid: "+billboardid);
                    id = billboardid+3;
                    if(mImageFile !=null) {
                        mBillboardInfo.setPictureUrl(mImageFile);
                    }
                    mBillboardInfo.setBillboardId(id +""+getWifiMacAddress());
                    sp.edit().putInt("billboardid",id).commit();
                    mBillboardInfo.setTableName("billBoardInfo");
                    mBillboardInfo.setTodo("insert");
                    LogUtil.d(mBillboardInfo.getEndDate() +mBillboardInfo.getPictureUrl());
                    Network.uploadBillBoardInfo(mBillboardInfo);
                   // insert(mBillboardInfo);
                    if(mImagePath !=null) {
                        Network.UploadPictureOrVideoApi(AddAdActivity.this, new File(mImagePath), mBillboardInfo.getBillboardId(), mBillboardInfo.getBusinessPhone());
                    }else{
                        Toast.makeText(mContext,"图片不能为空",Toast.LENGTH_SHORT).show();
                    }
                    finish();
                   // createBillBoardInfo();
                   // Network.uploadBillBoardInfo(mBillboardInfo);


//                    Intent intent = new Intent(AddAdActivity.this, MainActivity.class);
//                    Bundle data = new Bundle();
//                    data.putString("image_path",mImagePath);
//                    data.putBoolean("is_login",true);
//                    data.putString("detail_addr",mAddAdDetailAddr.getText().toString());
//                    data.putString("device_name",mAddAdDeviceName.getText().toString());
//                    data.putString("phone_number",mAddAdPhoneNumebr.getText().toString());
//                    intent.putExtras(data);
//                    startActivity(intent);
//                    finish();
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
//        if(mAddAdDetailAddr != null){
//            mAddAdDetailAddr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    inputDetailAddr();
//                }
//            });
//        }
//
//        if(mAddAdDeviceName != null){
//            mAddAdDeviceName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    inputDeviceName();
//                }
//            });
//        }
//
//        if(mAddAdPhoneNumebr != null){
//            mAddAdPhoneNumebr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    inputPhoneNumber();
//                }
//            });
//        }
//        if(mAddAdValidTime !=null){
//            mAddAdValidTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                     timePickerDialog = new TimePickerDialog(AddAdActivity.this, new TimePickerDialog.DialogState() {
//                        @Override
//                        public void cancelDialog() {
//                            timePickerDialog.dismissDialog();
//                        }
//
//                        @Override
//                        public void confirmDialog() {
//                             mAddAdValidTime.setText(timePickerDialog.getTime());
//                            timePickerDialog.dismissDialog();
//                        }
//                    });
//                    timePickerDialog.show();
//                }
//            });
//        }

        mAddOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(AddAdActivity.this,AdAddItemActivity.class);
                startActivityForResult(intent,REQUEST_ADD_AD);
            }
        });
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
        if(mAddOpenTime !=null){
            mAddOpenTime.setOnClickListener(null);
        }

//        if(mAddAdDetailAddr != null){
//            mAddAdDetailAddr.setOnClickListener(null);
//        }
//
//        if(mAddAdDeviceName != null){
//            mAddAdDeviceName.setOnClickListener(null);
//        }
//
//        if(mAddAdPhoneNumebr != null){
//            mAddAdPhoneNumebr.setOnClickListener(null);
//        }
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
//    private void inputDetailAddr(){
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
//        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
//        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
//        title.setText("详细地址");
//        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
//        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mDetailAddr != null){
//                    mDetailAddr.dismiss();
//                }
//            }
//        });
//        Button ensure = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
//        ensure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mDetailAddr != null){
//                    String detailAddr = editor.getText().toString();
//                    if(!TextUtils.isEmpty(detailAddr)){
//                        mAddAdDetailAddr.setText(detailAddr);
//                        mDetailAddr.dismiss();
//                    }
//                }
//            }
//        });
//        mBuilder.setView(view);
//        mBuilder.setCancelable(true);
//        mDetailAddr = mBuilder.create();
//        mDetailAddr.show();
//    }

//    private void inputDeviceName(){
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
//        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
//        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
//        title.setText("设备名称");
//        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
//        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mDeviceName != null){
//                    mDeviceName.dismiss();
//                }
//            }
//        });
//        Button ensure = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
//        ensure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mDeviceName != null){
//                    String detailAddr = editor.getText().toString();
//                    if(detailAddr != null && !"".equals(detailAddr)){
//                        mAddAdDeviceName.setText(detailAddr);
//                        mDeviceName.dismiss();
//                    }
//                }
//            }
//        });
//        mBuilder.setView(view);
//        mBuilder.setCancelable(true);
//        mDeviceName = mBuilder.create();
//        mDeviceName.show();
//    }
//
//    private void inputPhoneNumber(){
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAdActivity.this);
//        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
//        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
//        title.setText("联系电话");
//        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
//        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mPhoneName != null){
//                    mPhoneName.dismiss();
//                }
//            }
//        });
//        Button ensure = (Button)view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
//        ensure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mPhoneName != null){
//                    String detailAddr = editor.getText().toString();
//                    if(detailAddr != null && !"".equals(detailAddr)){
//                        mAddAdPhoneNumebr.setText(detailAddr);
//                        mPhoneName.dismiss();
//                    }
//                }
//            }
//        });
//        mBuilder.setView(view);
//        mBuilder.setCancelable(true);
//        mPhoneName = mBuilder.create();
//        mPhoneName.show();
//    }

    class AdAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =null;
            ViewHolder viewHolder ;
            if(convertView ==null){
                view =LayoutInflater.from(mContext).inflate(R.layout.ad_show_item,null);
                viewHolder = new ViewHolder();
                viewHolder.mTv =view.findViewById(R.id.tv_show_item);
                view.setTag(viewHolder);
            }else{
                view =convertView;
                viewHolder =(ViewHolder) view.getTag();
            }
            LogUtil.d("view: "+viewHolder.mTv);
            viewHolder.mTv.setText(items.get(position));
            return view;
        }
    }

    public class ViewHolder{
        TextView mTv;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
//    billboardId varchar primary key,price long,address varchar,equipmentType varchar," +
//            "screenType varchar,screenWidth long,screenHeight long,startDate varchar,endDate varchar,startTime varchar,endTime varchar," +
//            "pictureUrl varchar,equipmentAttribute varchar,screenAttritute varchar,businessPhone varchar,longitude double,latitude double)"
    public void insert(BillboardInfo billboardInfo){
        SQLiteOpenHelper sqLiteOpenHelper =new MySqliteHelper(this);
        SQLiteDatabase sqLiteDatabase =sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("billboardId",billboardInfo.getBillboardId());
        contentValues.put("price",billboardInfo.getPrice());
        contentValues.put("address",billboardInfo.getAddress());
        contentValues.put("equipmentType",billboardInfo.getEquipmentType());
        contentValues.put("screenType",billboardInfo.getScreenType());
        contentValues.put("screenWidth",billboardInfo.getScreenWidth());
        contentValues.put("screenHeight",billboardInfo.getScreenHeight());
        contentValues.put("startDate",billboardInfo.getStartDate());
        contentValues.put("endDate",billboardInfo.getEndDate());
        contentValues.put("startTime",billboardInfo.getStartTime());
        contentValues.put("endTime",billboardInfo.getEndTime());
        contentValues.put("pictureUrl",billboardInfo.getPictureUrl());
        contentValues.put("equipmentAttribute",billboardInfo.getEquipmentAttribute());
        contentValues.put("screenAttritute",billboardInfo.getScreenAttritute());
        contentValues.put("longitude",billboardInfo.getLongitude());
        contentValues.put("latitude",billboardInfo.getLatitude());
        sqLiteDatabase.insert("billboard",null,contentValues);
        sqLiteDatabase.close();
    }


}
