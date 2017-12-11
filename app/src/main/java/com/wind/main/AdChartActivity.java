package com.wind.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.wind.main.util.ChartUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyuqiang on 17-11-19.
 */

public class AdChartActivity extends AppCompatActivity {

    private LineChart mDateChart;
    private LineChart mTimeChart;
    private Button mCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wind.main.R.layout.ad_chart_layout);
        initView();
    }

    private void initView(){
        mCancel = (Button) findViewById(com.wind.main.R.id.ad_chart_cancel);
        mDateChart = (LineChart) findViewById(com.wind.main.R.id.ad_date_chart);
        mTimeChart = (LineChart) findViewById(com.wind.main.R.id.ad_time_chart);

        ChartUtils.initChart(mDateChart);
        ChartUtils.notifyDataSetChanged(mDateChart, getData(), ChartUtils.dayValue);

        ChartUtils.initChart(mTimeChart);
        ChartUtils.notifyDataSetChanged(mTimeChart, getData(), ChartUtils.dayValue);
    }

    private void applyListener(){
        if(mCancel != null){
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AdChartActivity.this,PriceDateActivity.class));
                    AdChartActivity.this.finish();
                }
            });
        }
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

    private List<Entry> getData() {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 15));
        values.add(new Entry(1, 15));
        values.add(new Entry(2, 15));
        values.add(new Entry(3, 20));
        values.add(new Entry(4, 25));
        values.add(new Entry(5, 20));
        values.add(new Entry(6, 20));
        return values;
    }
}
