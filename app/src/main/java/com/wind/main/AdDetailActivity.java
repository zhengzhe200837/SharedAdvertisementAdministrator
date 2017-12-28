package com.wind.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wind.main.adapter.CirCleTimeAdapter;
import com.wind.main.mode.CircleTimeItem;
import com.wind.main.mode.OrderFormItem;
import com.wind.main.network.Network;
import com.wind.main.network.api.UploadBillBoardInfoApi;
import com.wind.main.network.model.OrderInfo;
import com.wind.main.util.EnumUtil;
import com.wind.main.util.LogUtil;
import com.wind.main.util.TimeUtils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
 * Created by zhuyuqiang on 17-11-18.
 */

public class AdDetailActivity extends AppCompatActivity {

    private ActionBar mSupportActionBar;
    private View mActionBarView;
    private int mCurrentSelectMode = 1;
    private long mTimeBase = 0l;
    private Button mDay1,mDay2,mDay3,mDay4,mDay5,mDay6,mDay7,mAdDetailCancel;
    private TextView mLastTime,mCurrentTime,mNextTime;
    private Spinner mTimeSelector;
    private UIListener mListener;
    private RelativeLayout mListContent;
    private List<OrderFormItem> mOrderFormItems;
    private AdDetailListAdapter mAdDetailListAdapter;
    private List<CircleTimeItem> mCircleTimeItems;
    private CirCleTimeAdapter mCircleTimeAdapter;
    private Context context;
    private static String billboardId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.ad_detail_layout);
        context =this;
        initView();
        mListener = new UIListener();
        changeListViewContent(mCurrentSelectMode);
    }

    private void initView(){
        generatorCircleTime();
        mCircleTimeAdapter = new CirCleTimeAdapter(AdDetailActivity.this,mCircleTimeItems);
        mDay1 = (Button) findViewById(R.id.ad_detail_monday);
        mDay2 = (Button)findViewById(R.id.ad_detail_tuesday);
        mDay3 = (Button)findViewById(R.id.ad_detail_wednesday);
        mDay4 = (Button)findViewById(R.id.ad_detail_thursday);
        mDay5 = (Button)findViewById(R.id.ad_detail_friday);
        mDay6 = (Button)findViewById(R.id.ad_detail_saturday);
        mDay7 = (Button)findViewById(R.id.ad_detail_sunday);
        mTimeSelector = (Spinner) findViewById(R.id.time_selector);
        mAdDetailCancel = (Button) findViewById(R.id.ad_detail_cancel);
        mLastTime = (TextView)findViewById(R.id.ad_detail_last_time);
        mCurrentTime = (TextView)findViewById(R.id.ad_detail_current_time);
        mNextTime = (TextView)findViewById(R.id.ad_detail_next_time);
        mListContent = (RelativeLayout)findViewById(R.id.list_content);
        mTimeSelector.setAdapter(mCircleTimeAdapter);
        mTimeSelector.setSelection((mCircleTimeItems.size())/2);
        orderInfos =new ArrayList<>();
        orderInfo =new OrderInfo();
        orderInfo.setBillboardId(getIntent().getStringExtra("billboard_id"));
        billboardId =getIntent().getStringExtra("billboard_id");
        updateBackground(TimeUtils.getDayOfWeek(System.currentTimeMillis()));
        updateButtonContent();
    }

    private void initActionBar(){
        mSupportActionBar = getSupportActionBar();
        if(mSupportActionBar != null){
            mActionBarView = LayoutInflater.from(this).inflate(R.layout.ad_detail_action_bar_layout,null);
            ((TextView)(mActionBarView.findViewById(R.id.ad_detail_bar_title))).setText(getIntent().getStringExtra("device_name"));
            mActionBarView.findViewById(R.id.ad_detail_bar_select).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(AdDetailActivity.this, v);
                    popup.getMenuInflater().inflate(R.menu.poupup_menu_home, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.ad_menu_all:
                                    mCurrentSelectMode = EnumUtil.SELECT_ALL;
                                    break;
                                case R.id.ad_menu_display:
                                    mCurrentSelectMode = EnumUtil.SELECT_DISPLAY;

                                    break;
                                case R.id.ad_menu_need_upload:
                                    mCurrentSelectMode = EnumUtil.SELECT_UPLOAD;
                                    break;
                                case R.id.ad_menu_need_update:
                                    mCurrentSelectMode = EnumUtil.SELECT_UPDATE;
                                    break;
                                case R.id.ad_menu_need_check:
                                    mCurrentSelectMode = EnumUtil.SELECT_CHECK;
                                    break;
                            }
                            Intent intent =new Intent(AdDetailActivity.this,SelectAdByConditionActivity.class);
                            intent.putExtra("currentSelectMode",mCurrentSelectMode);
                            LogUtil.d("billboard: "+billboardId);
                            intent.putExtra("billboardId",billboardId);
                            startActivity(intent);
                            updateUI();
                            return true;
                        }
                    });
                    popup.show();
                }
            });
            mSupportActionBar.setCustomView(mActionBarView);
            mSupportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        }
    }

    private void updateUI() {
        if(isCurrentCircleTime((CircleTimeItem) mTimeSelector.getSelectedItem())){
            mCurrentSelectMode = TimeUtils.getDayOfWeek(System.currentTimeMillis());
        }else{
            mCurrentSelectMode = 1;
        }
        updateBackground(mCurrentSelectMode);
        updateButtonContent();
    }

    private void updateUI(long time) {
        if(isCurrentCircleTime(time)){
            mCurrentSelectMode = TimeUtils.getDayOfWeek(System.currentTimeMillis());
        }else{
            mCurrentSelectMode = 1;
        }
        updateBackground(mCurrentSelectMode);
        updateButtonContent();
    }

    private void updateBackground(int selectMode){
        clearAllBackground();
        LogUtil.e("zyq","select mode = "+selectMode);
        switch (selectMode){
            case EnumUtil.SELECT_DAY_1:
                mDay1.setBackgroundResource(R.color.colorAccent);
                break;
            case EnumUtil.SELECT_DAY_2:
                mDay2.setBackgroundResource(R.color.colorAccent);
                break;
            case EnumUtil.SELECT_DAY_3:
                mDay3.setBackgroundResource(R.color.colorAccent);
                break;
            case EnumUtil.SELECT_DAY_4:
                mDay4.setBackgroundResource(R.color.colorAccent);
                break;
            case EnumUtil.SELECT_DAY_5:
                mDay5.setBackgroundResource(R.color.colorAccent);
                break;
            case EnumUtil.SELECT_DAY_6:
                mDay6.setBackgroundResource(R.color.colorAccent);
                break;
            case EnumUtil.SELECT_DAY_7:
                mDay7.setBackgroundResource(R.color.colorAccent);
                break;
        }
    }

    private void clearAllBackground(){
        if(mDay1 != null){
            mDay1.setBackgroundResource(android.R.color.transparent);
        }

        if(mDay2 != null){
            mDay2.setBackgroundResource(android.R.color.transparent);
        }

        if(mDay3 != null){
            mDay3.setBackgroundResource(android.R.color.transparent);
        }

        if(mDay4 != null){
            mDay4.setBackgroundResource(android.R.color.transparent);
        }

        if(mDay5 != null){
            mDay5.setBackgroundResource(android.R.color.transparent);
        }

        if(mDay6 != null){
            mDay6.setBackgroundResource(android.R.color.transparent);
        }

        if(mDay7 != null){
            mDay7.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void applyListener(){
        if(mDay1 != null){
            mDay1.setOnClickListener(mListener);
        }

        if(mDay2 != null){
            mDay2.setOnClickListener(mListener);
        }

        if(mDay3 != null){
            mDay3.setOnClickListener(mListener);
        }

        if(mDay4 != null){
            mDay4.setOnClickListener(mListener);
        }

        if(mDay5 != null){
            mDay5.setOnClickListener(mListener);
        }

        if(mDay6 != null){
            mDay6.setOnClickListener(mListener);
        }

        if(mDay7 != null){
            mDay7.setOnClickListener(mListener);
        }

        if(mLastTime != null){
            mLastTime.setOnClickListener(mListener);
        }

        if(mCurrentTime != null){
            mCurrentTime.setOnClickListener(mListener);
        }

        if(mNextTime != null){
            mNextTime.setOnClickListener(mListener);
        }

        if(mTimeSelector != null){
            mTimeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mTimeBase = mCircleTimeItems.get(position).getTimeTamp();
                    updateButtonContent();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void updateButtonContent(){
        for(int i = 1;i<8;i++){
            String date = TimeUtils.getDateByFormat(mTimeBase+(i-1)*TimeUtils.DAY_TIME,TimeUtils.DATE_MONTH_DAY);
            LogUtil.d("date: "+date);
            switch (i){
                case EnumUtil.SELECT_DAY_1:
                    mDay1.setText(date+"\n"+getString(R.string.ad_monday));
                    break;
                case EnumUtil.SELECT_DAY_2:
                    mDay2.setText(date+"\n"+getString(R.string.ad_tuesday));
                    break;
                case EnumUtil.SELECT_DAY_3:
                    mDay3.setText(date+"\n"+getString(R.string.ad_wednesday));
                    break;
                case EnumUtil.SELECT_DAY_4:
                    mDay4.setText(date+"\n"+getString(R.string.ad_thursday));
                    break;
                case EnumUtil.SELECT_DAY_5:
                    mDay5.setText(date+"\n"+getString(R.string.ad_friday));
                    break;
                case EnumUtil.SELECT_DAY_6:
                    mDay6.setText(date+"\n"+getString(R.string.ad_saturday));
                    break;
                case EnumUtil.SELECT_DAY_7:
                    mDay7.setText(date+"\n"+getString(R.string.ad_sunday));
                    break;
            }
        }
    }

    private void generatorCircleTime(){
        long time = TimeUtils.getCurrentTimeOfWeek(System.currentTimeMillis());
        LogUtil.d("time: "+time);
        mCurrentSelectMode = TimeUtils.getDayOfWeek(System.currentTimeMillis());
        LogUtil.d("select : "+mCurrentSelectMode);
        mCircleTimeItems = new ArrayList<>();
        for (int i = 8;i>0;i--){
            CircleTimeItem item1 = new CircleTimeItem();
            item1.setTimeTamp(time-i*TimeUtils.WEEK_TIME);
            item1.setStartTime(TimeUtils.dateToString(time-i*TimeUtils.WEEK_TIME));
            item1.setEndTime(TimeUtils.dateToString(time-(i-1)*(TimeUtils.WEEK_TIME)-TimeUtils.DAY_TIME));
            mCircleTimeItems.add(item1);
        }
        CircleTimeItem item = new CircleTimeItem();
        item.setTimeTamp(time);
        item.setStartTime(TimeUtils.dateToString(time));
        item.setEndTime(TimeUtils.dateToString(time+(TimeUtils.WEEK_TIME)-TimeUtils.DAY_TIME));
        mCircleTimeItems.add(item);
        for (int i = 1;i<9;i++){
            CircleTimeItem item2 = new CircleTimeItem();
            item2.setTimeTamp(time+i*TimeUtils.WEEK_TIME);
            item2.setStartTime(TimeUtils.dateToString(time+i*TimeUtils.WEEK_TIME));
            item2.setEndTime(TimeUtils.dateToString(time+(i+1)*(TimeUtils.WEEK_TIME)-TimeUtils.DAY_TIME));
            mCircleTimeItems.add(item2);
        }
        mTimeBase = time;
    }

    private class UIListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ad_detail_monday:
                    changeListViewContent(EnumUtil.SELECT_DAY_1);
                    break;
                case R.id.ad_detail_tuesday:
                    changeListViewContent(EnumUtil.SELECT_DAY_2);
                    break;
                case R.id.ad_detail_wednesday:
                    changeListViewContent(EnumUtil.SELECT_DAY_3);
                    break;
                case R.id.ad_detail_thursday:
                    changeListViewContent(EnumUtil.SELECT_DAY_4);
                    break;
                case R.id.ad_detail_friday:
                    changeListViewContent(EnumUtil.SELECT_DAY_5);
                    break;
                case R.id.ad_detail_saturday:
                    changeListViewContent(EnumUtil.SELECT_DAY_6);
                    break;
                case R.id.ad_detail_sunday:
                    changeListViewContent(EnumUtil.SELECT_DAY_7);
                    break;
                case R.id.ad_detail_last_time:
                    int p1 = mTimeSelector.getSelectedItemPosition();
                    if(p1>0){
                        mTimeSelector.setSelection(p1-1);
                        mTimeBase = mCircleTimeItems.get(p1-1).getTimeTamp();
                    }
//                    updateUI();
                    updateUI(mTimeBase);
                    break;
                case R.id.ad_detail_current_time:
                    int p3 = mCircleTimeItems.size()/2;
                    mTimeSelector.setSelection(p3);
                    mTimeBase = mCircleTimeItems.get(p3).getTimeTamp();
//                    changeToCurrent();
                    changeToCurrent(mTimeBase);
                    break;
                case R.id.ad_detail_next_time:
                    int p2 = mTimeSelector.getSelectedItemPosition();
                    if(p2< mCircleTimeItems.size()){
                        mTimeSelector.setSelection(p2+1);
                        mTimeBase = mCircleTimeItems.get(p2+1).getTimeTamp();
                    }
//                    updateUI();
                    updateUI(mTimeBase);
                    break;
            }
        }
    }

    public void cancelAdDetailActivity(View view){
        LogUtil.e("zyq","AdDetailActivity.this.finish()");
        AdDetailActivity.this.finish();
    }

    private void changeToCurrent() {
        if(isCurrentCircleTime((CircleTimeItem) mTimeSelector.getSelectedItem())){
            mCurrentSelectMode = TimeUtils.getDayOfWeek(System.currentTimeMillis());
        }else{
            mTimeSelector.setSelection(mCircleTimeItems.size()/2);
        }
        updateBackground(mCurrentSelectMode);
        updateButtonContent();
    }

    private void changeToCurrent(long time){
        if(isCurrentCircleTime(time)){
            mCurrentSelectMode = TimeUtils.getDayOfWeek(System.currentTimeMillis());
        }else{
            mTimeSelector.setSelection(mCircleTimeItems.size()/2);
        }
        updateBackground(mCurrentSelectMode);
        updateButtonContent();
    }

    private boolean isCurrentCircleTime(CircleTimeItem item){
        long time = System.currentTimeMillis();
        if(time-item.getTimeTamp()>0 && time-item.getTimeTamp()< TimeUtils.WEEK_TIME){
            return true;
        }
        return false;
    }

    private boolean isCurrentCircleTime(long time){
        long currentTime = System.currentTimeMillis();
        if(currentTime-time>0 && currentTime-time< TimeUtils.WEEK_TIME){
            return true;
        }
        return false;
    }

    private void changeListViewContent(int select_day){
        mListContent.removeAllViews();
      //  generator();
        ListView mListView = new ListView(AdDetailActivity.this);
        mAdDetailListAdapter = new AdDetailListAdapter();
        mListView.setAdapter(mAdDetailListAdapter);
        mListContent.addView(mListView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityByStatus(position);
            }
        });
        updateBackground(select_day);
    }

    private void startActivityByStatus(int position) {
        int status = orderInfos.get(position).getOrderStatus();
        LogUtil.e("ad","order-status = "+status);
        Intent intent = new Intent(this,OrderFormActivity.class);
        intent.putExtra("order_status",status);
        intent.putExtra("media_name",orderInfos.get(position).getMediaName());
        intent.putExtra("order_id",orderInfos.get(position).getOrderId());
        startActivity(intent);
    }

    private int status[] = {1,2,3,4};

    private void generator(){
        mOrderFormItems = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i< 9;i++){
            OrderFormItem item = new OrderFormItem();
            item.setOrderDate("2017-11-13");
            item.setOrderName("订单"+(++i));
            i--;
            item.setOrderStatus(status[i%4]);
            item.setTimes(random.nextInt(3));
            item.setOrderTime("xxxxxx-xxxxxx");
            mOrderFormItems.add(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyListener();
    }

    @Override
    protected void onResume() {
        getOrderInfo("orderInfo","query");
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
    }

    private void cancelListener(){
        if(mDay1 != null){
            mDay1.setOnClickListener(null);
        }

        if(mDay2 != null){
            mDay2.setOnClickListener(null);
        }

        if(mDay3 != null){
            mDay3.setOnClickListener(null);
        }

        if(mDay4 != null){
            mDay4.setOnClickListener(null);
        }

        if(mDay5 != null){
            mDay5.setOnClickListener(null);
        }

        if(mDay6 != null){
            mDay6.setOnClickListener(null);
        }

        if(mDay7 != null){
            mDay7.setOnClickListener(null);
        }

        if(mLastTime != null){
            mLastTime.setOnClickListener(null);
        }

        if(mCurrentTime != null){
            mCurrentTime.setOnClickListener(null);
        }

        if(mNextTime != null){
            mNextTime.setOnClickListener(null);
        }
    }

    private List<OrderInfo> orderInfos;
    private OrderInfo orderInfo;
     private UploadBillBoardInfoApi mUploadBillBoardInfoApi;
    public  List<OrderInfo> getOrderInfo(String tableName,String todo){
        if(mUploadBillBoardInfoApi==null){
            mUploadBillBoardInfoApi = Network.getUploadBillBoardInfoApi();
        }
        orderInfo.setTableName(tableName);
        orderInfo.setTodo(todo);
        orderInfo.setMethod("queryByBillboardId");
        mUploadBillBoardInfoApi.getOrderInfo(orderInfo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<OrderInfo>>() {
            @Override
            public void accept(@NonNull List<OrderInfo> orderInfos1) throws Exception {
                LogUtil.d("xx: "+orderInfos1.size());
                orderInfos =orderInfos1;
                mAdDetailListAdapter.notifyDataSetChanged();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LogUtil.d("throwable: "+throwable.getMessage());
                Toast.makeText(AdDetailActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return orderInfos;
    }

    public class AdDetailListAdapter extends BaseAdapter {

//        private Context mContext;
//        private List<OrderInfo> mItems;
//
//        public AdDetailListAdapter(Context context, List<OrderInfo> items){
//            LogUtil.d("AdDetailListAdapter" +items);
//            this.mContext = context;
//            this.mItems = items;
//        }
        @Override
        public int getCount() {
            LogUtil.d("getCount" +orderInfos.size());
            return orderInfos.size();
        }

        @Override
        public Object getItem(int position) {
            LogUtil.d("getItem"+position);
            return orderInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            LogUtil.d("getItemId" +position);
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogUtil.d("position: "+position);
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.ad_detail_item_layout,null);
                holder.orderName = (TextView) convertView.findViewById(R.id.ad_detail_order_name);
                holder.orderDate = (TextView)convertView.findViewById(R.id.ad_detail_order_date);
                holder.orderTime = (TextView)convertView.findViewById(R.id.ad_detail_order_time);
                holder.times = (TextView)convertView.findViewById(R.id.ad_detail_order_count);
                holder.orderStatus = (TextView)convertView.findViewById(R.id.ad_detail_order_status);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(holder,position);
            return convertView;
        }

        private void bindView(ViewHolder holder, int position) {
            holder.orderName.setText(getResources().getString(R.string.order_name,position));
            String playStartTime =orderInfos.get(position).getPlayStartTime().substring(0,8);
            holder.orderDate.setText(getDate(playStartTime));
            holder.orderTime.setText(getTime(orderInfos.get(position).getPlayStartTime().substring(8,14)));
            holder.times.setText(context.getResources().getString(R.string.ad_detail_order_count,orderInfos.get(position).getPlayTimes()));
            holder.orderStatus.setText(getOrderStatusMessage(orderInfos.get(position).getOrderStatus()));
        }

        public String  getDate(String date){
            StringBuffer stringBuffer =new StringBuffer();
            stringBuffer.append(date.substring(0,4)+"-").append(date.substring(4,6)+"-").append(date.substring(6,8));
            LogUtil.d("xx"+stringBuffer.toString());
            return stringBuffer.toString();
        }
        public String getTime(String time){
            LogUtil.d("xx"+time);
            StringBuffer stringBuffer =new StringBuffer();
            stringBuffer.append(time.substring(0,2)+":").append(time.substring(2,4)+":").append(time.substring(4,6));
            return stringBuffer.toString();
        }

        private String getOrderStatusMessage(int status){
            switch (status){
                case ORDER_STATUS_DISPLAY:
                    return context.getResources().getString(R.string.ad_order_status_display);
                case ORDER_STATUS_PLAYED:
                    return context.getResources().getString(R.string.ad_order_status_played);
                case ORDER_STTUS_NOT_PASS:
                    return context.getResources().getString(R.string.ad_order_status_check_not_pass);
                case ORDER_STATUS_UPLOAD:
                    return context.getResources().getString(R.string.ad_order_status_upload);

                case ORDER_STATUS_UPDATE:
                    return context.getResources().getString(R.string.ad_order_status_update);

                case ORDER_STATUS_CHECK:
                    return context.getResources().getString(R.string.ad_order_status_check);

                default:
                    return context.getResources().getString(R.string.ad_order_status_display);
            }
        }

        class ViewHolder{
            TextView orderName;
            TextView orderDate;
            TextView orderTime;
            TextView times;
            TextView orderStatus;
        }
    }

}
