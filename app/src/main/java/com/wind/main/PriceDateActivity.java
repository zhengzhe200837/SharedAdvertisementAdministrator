package com.wind.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.wind.main.util.LogUtil;

/**
 * Created by zhuyuqiang on 17-11-19.
 */

public class PriceDateActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private Button mCancel;
    private float mDown = 0.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wind.main.R.layout.ad_price_date_review);
        initView();
    }

    private void initView(){
        mCalendarView = (CalendarView) findViewById(com.wind.main.R.id.ad_price_review_calendar);
        mCancel = (Button) findViewById(com.wind.main.R.id.ad_price_date_cancel);
    }

    private void applyListener(){
        if(mCalendarView != null){
            mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    LogUtil.e("zyq","date = " + year + "-"+ (month+1) + "-" + dayOfMonth);
                    String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                    Intent intent = new Intent(PriceDateActivity.this, PriceTimeActivity.class);
                    intent.putExtra("price_date",date);
                    startActivity(intent);
                }
            });
        }

        if(mCancel != null){
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PriceDateActivity.this.finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyListener();
    }

    private void cancelListener(){
        if(mCalendarView != null){
            mCalendarView.setOnDateChangeListener(null);
        }

        if(mCancel != null){
            mCancel.setOnClickListener(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
    }

    private boolean isStarted = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("zyq","distance = "+(event.getRawY() - mDown));
                if(event.getRawY() - mDown > 300.0f){
                    if(!isStarted){
                        startActivity(new Intent(PriceDateActivity.this, AdChartActivity.class));
                        PriceDateActivity.this.finish();
                        isStarted = true;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mDown = 0.0f;
                break;
        }
        return super.onTouchEvent(event);
    }
}
