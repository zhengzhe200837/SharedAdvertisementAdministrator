package com.wind.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xiaoxiao on 2017/12/25.
 */

public class MyWalleteActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.add_ad_Theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_layout);
    }
}
