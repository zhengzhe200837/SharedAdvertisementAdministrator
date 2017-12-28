package com.wind.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wind.main.view.ZzHorizontalCalenderView;

/**
 * Created by xiaoxiao on 2017/12/20.
 */

public class TimePickerActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(new ZzHorizontalCalenderView(this));
    }
}
