package com.wind.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.wind.main.adapter.AdListAdapter;
import com.wind.main.manager.NetWorkManager;
import com.wind.main.manager.SDPermissionManager;
import com.wind.main.mode.AdListItem;
import com.wind.main.network.Network;
import com.wind.main.network.api.UploadBillBoardInfoApi;
import com.wind.main.network.model.BillboardAndOrder;
import com.wind.main.network.model.BillboardInfo;
import com.wind.main.util.LogUtil;
import com.wind.main.util.MySqliteHelper;
import com.wind.main.util.VideoAndBitmapUtil;

import org.afinal.simplecache.ACache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ListView mAdListView;
    private View mActionBarView;
    private ActionBar mSupportActionBar;
    private AdListAdapter mAdListAdapter;
    private View mFootView;
//    private List<AdListItem> mAdListItems;
    private Button mPriceReview;
    private boolean isLogin = false;
    private SDPermissionManager mSDPermissionManager;
    private Context mContext;
    private ACache aCache ;
    private SDPermissionManager.RequestPermission mRequest = new SDPermissionManager.RequestPermission() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onRequestPermission(String[] permissions) {
            ActivityCompat.requestPermissions(MainActivity.this,permissions,0);
        }
    };
    BillboardInfo billboardInfo;

    private SDPermissionResult mPermissionResult;
    public interface SDPermissionResult{
        void onPermissionResult(String[] permissions, int[] grantResults);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =this;
        mSDPermissionManager = new SDPermissionManager(MainActivity.this);
        mSDPermissionManager.setRequestPermission(mRequest);
        setContentView(com.wind.main.R.layout.activity_main);
        aCache =ACache.get(getApplicationContext());
        mSupportActionBar = getSupportActionBar();
        mPriceReview = (Button) findViewById(com.wind.main.R.id.ad_price_review);
        if(mSupportActionBar != null){
            initActionBarView();
            mSupportActionBar.setCustomView(mActionBarView);
            mSupportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        }
        mSDPermissionManager.startCheckPermission();
        LogUtil.e("zyq","network = "+ NetWorkManager.isValidNetWork(MainActivity.this));
    }

    public void onResume(){
        super.onResume();
        isLogin =(aCache.getAsObject("is_login")==null?false:true);
        mPriceReview.setVisibility(isLogin?View.VISIBLE:View.INVISIBLE);
        initListView();
        LogUtil.d("onResume");
    }

    public boolean getIsLogin(){
        Bundle data = getIntent().getExtras();
        boolean loginIs =false;
        if(data != null){
            loginIs = data.getBoolean("is_login",false);
        }
        return loginIs;
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
        billboardInfos = new ArrayList<>();
        initFootView();
      //  mAdListItems = new ArrayList<>();
        LogUtil.d("lsLogin" +isLogin);
        if(isLogin) {
            //generator();
            SelectBillboardInfo("billBoardInfo","query");
        }
        mAdListView = (ListView) findViewById(com.wind.main.R.id.ad_list_view);
//        mAdListAdapter = new AdListAdapter(MainActivity.this, mAdListItems);
        adAdapter =new AdAdapter();
        mAdListView.setAdapter(adAdapter);
        LogUtil.d("footView: "+mAdListView.getFooterViewsCount());
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
        if(position<0 || position>billboardInfos.size()){
            return;
        }
        Intent intent = new Intent(MainActivity.this,AdDetailActivity.class);
        intent.putExtra("billboard_id",billboardInfos.get(position).getBillboardId());
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
                LogUtil.d("login: "+aCache.getAsObject("is_login"));
                if(aCache.getAsObject("is_login")!=null){
                    startSubActivity(MainActivity.this, UserInfoActivity.class);
                }else{
                    Toast.makeText(MainActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }

            }
        });
        mActionBarView.findViewById(com.wind.main.R.id.more_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    private void generator(){
//        Random random = new Random();
//        if(mAdListItems ==null)
//        mAdListItems = new ArrayList<>();
//        if(billboardInfo !=null){
//            billboardInfo.getEquipmentType();
//        }
//
//        for(int i = 0;i<3;i++){
//            AdListItem item = new AdListItem();
//            item.setAdDevice("广告牌"+(++i));
//            i--;
//            item.setCount(random.nextInt(10));
//            item.setDetailAddr("***************");
//            item.setPhoneNumber("180XXXXXXXX");
//            item.setIcon(BitmapFactory.decodeResource(getResources(), com.wind.main.R.mipmap.ic_launcher));
//            mAdListItems.add(item);
//        }
//    }

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
//        AdListItem item = parserIntent();
//        if(item != null){
//            LogUtil.e("zyq","add(mNewItem)");
//            mAdListItems.add(item);
//            mAdListAdapter.notifyDataSetChanged();
//        }
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

//    public List<BillboardInfo>query(){
//        billboardInfos =Network.SelectBillboardInfo("billBoardInfo","query");
//        return billboardInfos;
//    }
//    public BillboardInfo getBillboardInfo(){
//        BillboardInfo billboardInfo =Network.queryBillboardInfo("billBoardInfo","query");
//        LogUtil.d("billboardinfo: "+billboardInfo);
//        return billboardInfo;
//    }

//    public List<BillboardInfo> getBillboardInfos(){
//        MySqliteHelper mySqliteHelper =new MySqliteHelper(this);
//        SQLiteDatabase sq1iteDdatabase =mySqliteHelper.getReadableDatabase();
//        int count =0;
//        Cursor cursor =sq1iteDdatabase.query("billboard",new String[]{"billboardId","equipmentType"},null,null,null,null,null,null);
//        while(cursor.moveToNext()){
//            String billboardId =cursor.getColumnName(cursor.getColumnIndex("billboardId"));
//            String equipmentType =cursor.getColumnName(cursor.getColumnIndex("equipmentType"));
//            Cursor orderCursor =sq1iteDdatabase.query("orderinfo",null,"where billboardId =?" ,new String[]{billboardId},null,null,null,null);
//            while (cursor.moveToNext()){
//                count =cursor.getCount();
//            }
//            BillboardAndOrder billboardAndOrder =new BillboardAndOrder();
//            billboardAndOrder.setCount(count);
//            billboardAndOrder.setEquipmentType(equipmentType);
//
//        }
//    }

    private UploadBillBoardInfoApi mUploadBillBoardInfoApi;
    private List<BillboardInfo> billboardInfos;
    public  List<BillboardInfo> SelectBillboardInfo(String tableName, String action) {
        if (mUploadBillBoardInfoApi == null) {
            mUploadBillBoardInfoApi = Network.getUploadBillBoardInfoApi();
        }
        BillboardInfo billboardInfo =new BillboardInfo();
        billboardInfo.setTableName(tableName);
        billboardInfo.setBusinessPhone("88888888888");
        billboardInfo.setMethod("getBillboardByPhone");
        billboardInfo.setTodo(action);
        mUploadBillBoardInfoApi.getBillBoardInfo(billboardInfo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<BillboardInfo>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<BillboardInfo> billboardInfos1) throws Exception {
                LogUtil.d("annotations:");
                billboardInfos = billboardInfos1;
                adAdapter.notifyDataSetChanged();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                LogUtil.d("annotations: " + throwable.getMessage());
                Toast.makeText(MainActivity.this,""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        return billboardInfos;

    }
//    private List<BillboardAndOrder> billboardAndOrders;
//    public void convertUrlToBitmap(List<BillboardInfo> billboardInfos){
//        billboardAndOrders =new ArrayList<>();
//        for(BillboardInfo billboardInfo: billboardInfos){
//            ImageView imageView =null;
//            BillboardAndOrder billboardAndOrder =new BillboardAndOrder();
//                Glide.with(this).load(billboardAndOrder.getPictureUrl()).into(imageView);
//            if(imageView !=null) {
//                billboardAndOrder.setImageView(imageView);
//            }billboardAndOrder.setBillboardInfo(billboardInfo);
//            billboardAndOrders.add(billboardAndOrder);
//        }
//
//    }
    private AdAdapter adAdapter;
    public class AdAdapter extends BaseAdapter{
       // private List<BillboardInfo> mBillboardInfos;
        @Override
        public int getCount() {
            return billboardInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return billboardInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.ad_list_item_layout,null);
                holder.adListItemIcon = (ImageView) convertView.findViewById(R.id.ad_list_item_icon);
                holder.adListItemName = (TextView) convertView.findViewById(R.id.ad_list_item_name);
                holder.adListItemCount = (TextView)convertView.findViewById(R.id.ad_list_item_count);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(holder,position);
            return convertView;
        }
        private void bindView(ViewHolder holder,int pos){
            //holder.adListItemIcon.setImageBitmap(mBillboardAndOrder.get(pos).getIcon());
            Glide.with(MainActivity.this).load(billboardInfos.get(pos).getPictureUrl()).into(holder.adListItemIcon);
            holder.adListItemName.setText(billboardInfos.get(pos).getEquipmentName());
            holder.adListItemCount.setText(getResources().getString(R.string.order_name,billboardInfos.get(pos).getOrderCount()));
        }

        class ViewHolder {
            TextView adListItemCount;
            TextView adListItemName;
            ImageView adListItemIcon;
        }
    }
    }

