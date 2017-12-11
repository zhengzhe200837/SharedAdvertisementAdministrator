package com.wind.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wind.main.adapter.AdDetailListAdapter;
import com.wind.main.adapter.CirCleTimeAdapter;
import com.wind.main.mode.CircleTimeItem;
import com.wind.main.mode.OrderFormItem;
import com.wind.main.util.EnumUtil;
import com.wind.main.util.LogUtil;
import com.wind.main.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.ad_detail_layout);
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
        mCurrentSelectMode = TimeUtils.getDayOfWeek(System.currentTimeMillis());
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
        generator();
        ListView mListView = new ListView(AdDetailActivity.this);
        mAdDetailListAdapter = new AdDetailListAdapter(AdDetailActivity.this,mOrderFormItems);
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
        int status = mOrderFormItems.get(position).getOrderStatus();
        LogUtil.e("ad","order-status = "+status);
        Intent intent = new Intent(this,OrderFormActivity.class);
        intent.putExtra("order_status",status);
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

}
