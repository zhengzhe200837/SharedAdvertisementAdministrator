package com.wind.main;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.wind.main.util.LogUtil;

import static com.wind.main.util.EnumUtil.ORDER_STATUS_CHECK;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_DISPLAY;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPDATE;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPLOAD;

/**
 * Created by zhuyuqiang on 17-11-18.
 */

public class OrderFormActivity extends AppCompatActivity {

    private int mOrderStatus = -1;
    private RelativeLayout mOrderOperatorGroup;
    private View mOrderOperatorView;
    private Button mOrderBack,mCancel;
    private Button mUpload;
    private Button mCheckYes,mCheckNo;
    private UIListener mListener;
    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parserIntent(getIntent());
        setContentView(R.layout.order_form_layout);
        mOrderOperatorGroup = (RelativeLayout) findViewById(R.id.order_form_operator);
        initView();
        mListener = new UIListener();
    }

    private void parserIntent(Intent intent){
        int order_status = intent.getIntExtra("order_status",-1);
        String orderName = intent.getStringExtra("order_name");
        LogUtil.e("ad","order_status = "+order_status);
        switch (order_status){
            case ORDER_STATUS_DISPLAY:
                mOrderStatus = ORDER_STATUS_DISPLAY;
                initOrderStatusDisplay();
                break;
            case ORDER_STATUS_UPLOAD:
                mOrderStatus = ORDER_STATUS_UPLOAD;
                initOrderStatusUpLoad(orderName);
                break;
            case ORDER_STATUS_CHECK:
                mOrderStatus = ORDER_STATUS_CHECK;
                initOrderStatusCheck(orderName);
                break;
            case ORDER_STATUS_UPDATE:
                mOrderStatus = ORDER_STATUS_UPDATE;
                initOrderStatusUpLoad(orderName);
                break;
        }
    }

    private void initOrderStatusCheck(String orderName) {
        setTheme(R.style.AppTheme);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(orderName);
        }
    }

    private void initOrderStatusUpLoad(String orderName) {
        setTheme(R.style.AppTheme);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(orderName);
        }
    }

    private void initOrderStatusDisplay() {
        setTheme(R.style.add_ad_Theme);
    }

    private void initView(){
        switch (mOrderStatus){
            case ORDER_STATUS_DISPLAY:
                mOrderOperatorView = LayoutInflater.from(OrderFormActivity.this).inflate(R.layout.order_form_status_display,null);
                mCancel = (Button) mOrderOperatorView.findViewById(R.id.ad_order_display_cancel);
                mOrderBack = (Button)mOrderOperatorView.findViewById(R.id.ad_order_display_back);
                mOrderOperatorGroup.addView(mOrderOperatorView);
                break;
            case ORDER_STATUS_UPDATE:
                mOrderStatus = ORDER_STATUS_UPDATE;
            case ORDER_STATUS_UPLOAD:
                mOrderOperatorView = LayoutInflater.from(OrderFormActivity.this).inflate(R.layout.order_form_status_upload,null);
                mUpload = (Button)mOrderOperatorView.findViewById(R.id.order_form_upload_mark);
                mCancel = (Button)mOrderOperatorView.findViewById(R.id.ad_order_upload_cancel);
                mOrderOperatorGroup.addView(mOrderOperatorView);
                break;
            case ORDER_STATUS_CHECK:
                mOrderOperatorView = LayoutInflater.from(OrderFormActivity.this).inflate(R.layout.order_form_status_check,null);
                mCheckYes = (Button)mOrderOperatorView.findViewById(R.id.order_form_check_yes);
                mCheckNo = (Button)mOrderOperatorView.findViewById(R.id.order_form_check_no);
                mCancel = (Button)mOrderOperatorView.findViewById(R.id.ad_order_check_cancel);
                mOrderOperatorGroup.addView(mOrderOperatorView);
                break;
        }
        mVideoView = (VideoView) findViewById(R.id.order_form_video_view);
        if(mOrderStatus != ORDER_STATUS_UPLOAD){
            mVideoView.setVideoPath("/storage/emulated/0/DCIM/Camera/zhuyuqiang.3gp");
        }

    }

    private void applyListener(){

        if(mOrderBack != null){
            mOrderBack.setOnClickListener(mListener);
        }
        if(mCancel != null){
            mCancel.setOnClickListener(mListener);
        }
        if(mUpload != null){
            mUpload.setOnClickListener(mListener);
        }
        if(mCheckYes != null){
            mCheckYes.setOnClickListener(mListener);
        }
        if(mCheckNo != null){
            mCheckNo.setOnClickListener(mListener);
        }
        if(mVideoView != null){
            mVideoView.setOnClickListener(mListener);
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    mVideoView.seekTo(0);
                    mVideoView.start();
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        applyListener();
        if(mOrderStatus != ORDER_STATUS_UPLOAD){
            mVideoView.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
        if(mVideoView.isPlaying()){
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoView != null && mOrderStatus != ORDER_STATUS_UPLOAD){
            mVideoView.stopPlayback();
        }
    }

    private void cancelListener(){

        if(mOrderBack != null){
            mOrderBack.setOnClickListener(null);
        }
        if(mCancel != null){
            mCancel.setOnClickListener(null);
        }
        if(mUpload != null){
            mUpload.setOnClickListener(null);
        }
        if(mCheckYes != null){
            mCheckYes.setOnClickListener(null);
        }
        if(mCheckNo != null){
            mCheckNo.setOnClickListener(null);
        }
        if(mVideoView != null){
            mVideoView.setOnCompletionListener(null);
        }
    }

    private class UIListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ad_order_check_cancel:
                case R.id.ad_order_display_cancel:
                case R.id.ad_order_upload_cancel:
                    OrderFormActivity.this.finish();
                    break;
                case R.id.order_form_check_yes:
                    Toast.makeText(OrderFormActivity.this,R.string.ad_order_check_yes,Toast.LENGTH_LONG).show();
                    OrderFormActivity.this.finish();
                    break;
                case R.id.order_form_check_no:
                    Toast.makeText(OrderFormActivity.this,R.string.ad_order_check_no,Toast.LENGTH_LONG).show();
                    OrderFormActivity.this.finish();
                    break;
                case R.id.order_form_upload_mark:
                    Toast.makeText(OrderFormActivity.this,R.string.ad_order_upload_mark,Toast.LENGTH_LONG).show();
                    OrderFormActivity.this.finish();
                    break;
                case R.id.ad_order_display_back:
                    Toast.makeText(OrderFormActivity.this,R.string.ad_order_form_ensure,Toast.LENGTH_LONG).show();
                    OrderFormActivity.this.finish();
                    break;
                case R.id.order_form_video_view:
//                    OrderFormActivity.this.finish();
                    break;
            }
        }
    }
}
