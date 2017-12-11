package com.wind.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.wind.main.adapter.PriceTimeAdapter;
import com.wind.main.mode.PriceTimeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhuyuqiang on 17-11-19.
 */

public class PriceTimeActivity extends AppCompatActivity{

    private Button mCancel;
    private ListView mListView;
    private List<PriceTimeItem> mPriceTimeItems;
    private String mDate;
    private PriceTimeAdapter mPriceTimeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = getIntent().getStringExtra("price_date");
        setContentView(com.wind.main.R.layout.ad_price_time_layout);
        mCancel = (Button) findViewById(com.wind.main.R.id.ad_price_time_cancel);
        mListView = (ListView) findViewById(com.wind.main.R.id.ad_price_time_list);
        initList();
    }

    private void initList(){
        generator();
        mPriceTimeAdapter = new PriceTimeAdapter(PriceTimeActivity.this, mPriceTimeItems);
        mListView.setAdapter(mPriceTimeAdapter);
        mListView.setSelection(18*12);
    }

    private void applyListener(){
        if(mCancel != null){
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PriceTimeActivity.this.finish();
                }
            });
        }
    }

    private int status[] = {1,2,3,4,0};

    private void generator(){
        mPriceTimeItems = new ArrayList<>();
        int hours = 0;
        int minute = 0;
        int i = 0;
        Random r = new Random();
        do {
            i++;
            PriceTimeItem item = new PriceTimeItem();
            item.setDate(mDate);
            item.setTimeStart(hours,minute);
            minute += 5;
            if(minute >= 60){
                minute = minute-60;
                hours++;
            }
            item.setTimeStop(hours,minute);
            item.setTime_1_status(status[r.nextInt(10)%5]);
            item.setTime_2_status(status[r.nextInt(10)%5]);
            item.setTime_3_status(status[r.nextInt(10)%5]);
            item.setTime_4_status(status[r.nextInt(10)%5]);
            item.setTime_5_status(status[r.nextInt(10)%5]);
            item.setTime_6_status(status[r.nextInt(10)%5]);
            item.setTime_7_status(status[r.nextInt(10)%5]);
            item.setTime_8_status(status[r.nextInt(10)%5]);
            item.setTime_9_status(status[r.nextInt(10)%5]);
            item.setTime_10_status(status[r.nextInt(10)%5]);
            mPriceTimeItems.add(item);
        } while (i<(24*12));
    }



    @Override
    protected void onStart() {
        super.onStart();
        applyListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
    }

    private void cancelListener(){
        if(mCancel != null){
            mCancel.setOnClickListener(null);
        }
    }
}
