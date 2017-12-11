package com.wind.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.wind.main.adapter.AdListAdapter;
import com.wind.main.manager.NetWorkManager;
import com.wind.main.manager.SDPermissionManager;
import com.wind.main.mode.AdListItem;
import com.wind.main.util.LogUtil;
import com.wind.main.util.VideoAndBitmapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView mAdListView;
    private View mActionBarView;
    private ActionBar mSupportActionBar;
    private AdListAdapter mAdListAdapter;
    private View mFootView;
    private List<AdListItem> mAdListItems;
    private Button mPriceReview;
    private boolean isLogin = false;
    private SDPermissionManager mSDPermissionManager;

    private SDPermissionManager.RequestPermission mRequest = new SDPermissionManager.RequestPermission() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onRequestPermission(String[] permissions) {
            MainActivity.this.requestPermissions(permissions,0);
        }
    };


    private SDPermissionResult mPermissionResult;
    public interface SDPermissionResult{
        void onPermissionResult(String[] permissions, int[] grantResults);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSDPermissionManager = new SDPermissionManager(MainActivity.this);
        mSDPermissionManager.setRequestPermission(mRequest);
        setContentView(com.wind.main.R.layout.activity_main);
        mSupportActionBar = getSupportActionBar();
        mPriceReview = (Button) findViewById(com.wind.main.R.id.ad_price_review);
        if(mSupportActionBar != null){
            initActionBarView();
            mSupportActionBar.setCustomView(mActionBarView);
            mSupportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        }
        mSDPermissionManager.startCheckPermission();
        LogUtil.e("zyq","network = "+ NetWorkManager.isValidNetWork(MainActivity.this));
        initListView();
    }

    private AdListItem parserIntent() {
        Bundle data = getIntent().getExtras();
        LogUtil.e("zyq","parserIntent = "+(data != null));
        AdListItem item = null;
        if(data != null){
            isLogin = data.getBoolean("is_login",false);
            item = new AdListItem();
            String path = data.getString("image_path","");
            LogUtil.e("zyq","image_path = "+path);
            if(path == null || "".equals(path)){
                return null;
            }else{
                item.setIcon(getLocalVideoThumbnail(path));
            }
            item.setDetailAddr(data.getString("detail_addr",""));
            item.setAdDevice(data.getString("device_name",""));
            item.setPhoneNumber(data.getString("phone_number",""));
            item.setCount(0);
        }
        return item;
    }

    public Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = VideoAndBitmapUtil.getIconImageByBitmap(VideoAndBitmapUtil.getImageByVideoPath(filePath));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private void initListView(){
        initFootView();
        generator();
        mAdListView = (ListView) findViewById(com.wind.main.R.id.ad_list_view);
        mAdListAdapter = new AdListAdapter(MainActivity.this, mAdListItems);
        mAdListView.setAdapter(mAdListAdapter);
        if(mAdListView.getFooterViewsCount() == 0){
            mAdListView.addFooterView(mFootView);
        }
        mAdListView.setFooterDividersEnabled(true);
        mAdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAdDetailActivity(position);
            }
        });
    }

    private void startAdDetailActivity(int position) {
        if(position<0 || position>mAdListItems.size()){
            return;
        }
        Intent intent = new Intent(MainActivity.this,AdDetailActivity.class);
        intent.putExtra("device_name",mAdListItems.get(position).getAdDevice());
        startActivity(intent);
    }

    private void initFootView(){
        mFootView = LayoutInflater.from(this).inflate(com.wind.main.R.layout.ad_list_foot_view,null);
        mFootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogin){
                    startSubActivity(MainActivity.this, UserAuthenticationActivity.class);
                }else{
                    startSubActivity(MainActivity.this, AddAdActivity.class);
                }

            }
        });
    }

    private void initActionBarView(){
        mActionBarView = LayoutInflater.from(this).inflate(com.wind.main.R.layout.action_bar_layout,null);
        mActionBarView.findViewById(com.wind.main.R.id.user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity(MainActivity.this, UserInfoActivity.class);
            }
        });
        mActionBarView.findViewById(com.wind.main.R.id.more_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void generator(){
        Random random = new Random();
        mAdListItems = new ArrayList<>();
        for(int i = 0;i<3;i++){
            AdListItem item = new AdListItem();
            item.setAdDevice("广告牌"+(++i));
            i--;
            item.setCount(random.nextInt(10));
            item.setDetailAddr("***************");
            item.setPhoneNumber("180XXXXXXXX");
            item.setIcon(BitmapFactory.decodeResource(getResources(), com.wind.main.R.mipmap.ic_launcher));
            mAdListItems.add(item);
        }
    }

    private void startSubActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    private void applyListener(){
        if(mPriceReview != null) {
            mPriceReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,PriceDateActivity.class));
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        AdListItem item = parserIntent();
        if(item != null){
            LogUtil.e("zyq","add(mNewItem)");
            mAdListItems.add(item);
            mAdListAdapter.notifyDataSetChanged();
        }
        applyListener();
        LogUtil.e("zyq","onStart");
        mPriceReview.setVisibility(isLogin?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
    }

    private void cancelListener() {
        if(mPriceReview != null){
            mPriceReview.setOnClickListener(null);
        }
    }

    public void setPermissionResult(SDPermissionResult s){
        this.mPermissionResult = s;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPermissionResult != null){
            mPermissionResult.onPermissionResult(permissions,grantResults);
        }
    }
}
