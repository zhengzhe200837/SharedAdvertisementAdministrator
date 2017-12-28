package com.wind.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wind.main.network.Network;
import com.wind.main.network.api.UploadBillBoardInfoApi;
import com.wind.main.network.model.OrderInfo;
import com.wind.main.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.wind.main.util.EnumUtil.ORDER_STATUS_CHECK;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_DISPLAY;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_PLAYED;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPDATE;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPLOAD;
import static com.wind.main.util.EnumUtil.ORDER_STTUS_NOT_PASS;

/**
 * Created by xiaoxiao on 2017/12/25.
 */

public class SelectAdByConditionActivity extends AppCompatActivity implements View.OnClickListener{

    TextView ad_menu_all;
    TextView ad_menu_need_check;
    TextView ad_menu_need_upload;
    TextView ad_menu_display;
    TextView ad_menu_need_update;
    ListView mListView;
    private SelectAdByConditionAdapter selectAdByConditionAdapter;
    private static int currentMode =0;
    private Context mContext;
    private UploadBillBoardInfoApi mUploadBillboardInfoApi;
    private static String billboardId;
    List<OrderInfo> orderInfos;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(R.style.add_ad_Theme);
        setContentView(R.layout.ad_list_by_condition);
        initView();
        initData();
    }

    public void initData(){
        mContext =this;
        currentMode =getIntent().getIntExtra("currentSelectMode",0);
        billboardId =getIntent().getStringExtra("billboardId");
        LogUtil.d("currentMode: "+currentMode +" billboardId: "+billboardId);
        orderInfos =new ArrayList<>();
        selectAdByConditionAdapter =new SelectAdByConditionAdapter();
        mListView.setAdapter(selectAdByConditionAdapter);
    }
    public void onResume(){
        super.onResume();
        orderInfos("orderInfo","query");
        updateUI(currentMode);
    }
    public void initView(){
        ad_menu_all =(TextView)findViewById(R.id.ad_menu_all);
        ad_menu_need_check =(TextView)findViewById(R.id.ad_menu_need_check);
        ad_menu_need_upload =(TextView)findViewById(R.id.ad_menu_need_upload);
        ad_menu_display =(TextView)findViewById(R.id.ad_menu_display);
        ad_menu_need_update =(TextView)findViewById(R.id.ad_menu_need_update);
        mListView =(ListView)findViewById(R.id.lv_ad_by_select);
        ad_menu_all.setOnClickListener(this);
        ad_menu_need_check.setOnClickListener(this);
        ad_menu_need_upload.setOnClickListener(this);
        ad_menu_need_update.setOnClickListener(this);
        ad_menu_display.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        clearUi();
        switch (v.getId()){
            case R.id.ad_menu_all:
                currentMode =0;
                break;
            case R.id.ad_menu_need_check:
                currentMode=4;
                break;
            case R.id.ad_menu_need_upload:
                currentMode=2;
                break;
            case R.id.ad_menu_display:
                currentMode=1;
                break;
            case R.id.ad_menu_need_update:
                currentMode=3;
                break;
        }
        orderInfos("orderInfo","query");
        updateUI(currentMode);

    }

    public void clearUi(){
        ad_menu_all.setBackgroundResource(android.R.color.transparent);
        ad_menu_display.setBackgroundResource(android.R.color.transparent);
        ad_menu_need_upload.setBackgroundResource(android.R.color.transparent);
        ad_menu_need_update.setBackgroundResource(android.R.color.transparent);
        ad_menu_need_check.setBackgroundResource(android.R.color.transparent);

    }
   public void updateUI(int id){
       if(id ==0){
           ad_menu_all.setBackgroundResource(R.color.colorAccent);
       }else if(id ==1){
           ad_menu_display.setBackgroundResource(R.color.colorAccent);
       }else if(id==2){
           ad_menu_need_upload.setBackgroundResource(R.color.colorAccent);
       }else if(id ==3){
           ad_menu_need_update.setBackgroundResource(R.color.colorAccent);
       }else if(id ==4){
           ad_menu_need_check.setBackgroundResource(R.color.colorAccent);
       }
   }
    public class SelectAdByConditionAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return orderInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return orderInfos.get(position);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.ad_detail_item_layout,null);
                viewHolder =new ViewHolder();
                viewHolder.orderName = (TextView) view.findViewById(R.id.ad_detail_order_name);
                viewHolder.orderDate = (TextView)view.findViewById(R.id.ad_detail_order_date);
                viewHolder.orderTime = (TextView)view.findViewById(R.id.ad_detail_order_time);
                viewHolder.times = (TextView)view.findViewById(R.id.ad_detail_order_count);
                viewHolder.orderStatus = (TextView)view.findViewById(R.id.ad_detail_order_status);
                view.setTag(viewHolder);
            }else{
                view =convertView;
                viewHolder =(ViewHolder) convertView.getTag();
            }
            bindView(viewHolder,position);
            return  view;
        }

        public void bindView(ViewHolder viewHolder,int position){
            viewHolder.orderName.setText(getResources().getString(R.string.order_name,position));
            viewHolder.orderDate.setText(orderInfos.get(position).getPlayStartTime().substring(0,8));
            viewHolder.orderTime.setText(orderInfos.get(position).getPlayStartTime().substring(9,13));
            viewHolder.times.setText(mContext.getResources().getString(R.string.ad_detail_order_count,orderInfos.get(position).getPlayTimes()));
            viewHolder.orderStatus.setText(getOrderStatusMessage(orderInfos.get(position).getOrderStatus()));
        }

        private String getOrderStatusMessage(int status){
            switch (status){
                case ORDER_STATUS_DISPLAY:
                    return mContext.getResources().getString(R.string.ad_order_status_display);
                case ORDER_STATUS_PLAYED:
                    return mContext.getResources().getString(R.string.ad_order_status_played);
                case ORDER_STTUS_NOT_PASS:
                    return mContext.getResources().getString(R.string.ad_order_status_check_not_pass);
                case ORDER_STATUS_UPLOAD:
                    return mContext.getResources().getString(R.string.ad_order_status_upload);
                case ORDER_STATUS_UPDATE:
                    return mContext.getResources().getString(R.string.ad_order_status_update);

                case ORDER_STATUS_CHECK:
                    return mContext.getResources().getString(R.string.ad_order_status_check);

                default:
                    return mContext.getResources().getString(R.string.ad_order_status_display);
            }
        }
    }
    public class ViewHolder{
        TextView orderName;
        TextView orderDate;
        TextView orderTime;
        TextView times;
        TextView orderStatus;
    }

    public void orderInfos(String tableName,String todo){
        mUploadBillboardInfoApi = Network.getUploadBillBoardInfoApi();
        final OrderInfo orderInfo =new OrderInfo();
        orderInfo.setBillboardId(billboardId);
        orderInfo.setTableName(tableName);
        orderInfo.setTodo(todo);
        orderInfo.setMethod("queryByBillboardId");
        mUploadBillboardInfoApi.getOrderInfo(orderInfo).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<OrderInfo>>() {
            @Override
            public void accept(@NonNull List<OrderInfo> orderInfos1) throws Exception {
                orderInfos.clear();
                if(currentMode ==0){
                     orderInfos=orderInfos1;
                }else if(currentMode ==1){
                    for(OrderInfo orderInfo1 :orderInfos1){
                        if(orderInfo1.getOrderStatus() ==0){
                            orderInfos.add(orderInfo1);
                        }
                    }
                }else if(currentMode ==4){
                    for(OrderInfo orderInfo1 :orderInfos1){
                        if(orderInfo1.getOrderStatus() ==2){
                            orderInfos.add(orderInfo1);
                        }
                    }
                }
                selectAdByConditionAdapter.notifyDataSetChanged();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Toast.makeText(mContext,throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
