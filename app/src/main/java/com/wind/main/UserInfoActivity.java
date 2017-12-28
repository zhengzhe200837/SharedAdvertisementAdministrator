package com.wind.main;

/**
 * Created by zhuyuqiang on 17-11-18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserInfoActivity extends Activity implements OnClickListener{
    private TextView mTextView;
    RelativeLayout myAdShow;
    RelativeLayout myAccount;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wind.main.R.layout.activity_userinfo);
        mContext =this;
        myAdShow = (RelativeLayout) findViewById(R.id.ad_show);
        myAccount =(RelativeLayout)findViewById(R.id.user_account);
        myAdShow.setOnClickListener(this);
        myAccount.setOnClickListener(this);
//        myOrderLayout.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View arg0) {
////                Intent mIntent = new Intent(UserInfoActivity.this, MyOrderActivity.class);
////                startActivity(mIntent);
//            }
//        });

        mTextView = (TextView) findViewById(com.wind.main.R.id.cancle);

//        myAccount.setOnClickListener(new OnClickListener() {
//            public void onClick(View arg0) {
////                Intent mIntent = new Intent(UserInfoActivity.this, MyVideoActivity.class);
////                startActivity(mIntent);
//            }
//        });

        mTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.ad_show:
                Intent intent =new Intent();
                intent.setClass(this,MyAdActivity.class);
                startActivity(intent);
                break;
            case R.id.user_account:
                Intent intentAccount =new Intent();
                intentAccount.setClass(this,MyWalleteActivity.class);
                startActivity(intentAccount);
                break;
            case R.id.cancle:
                finish();
                break;
        }
    }
}
