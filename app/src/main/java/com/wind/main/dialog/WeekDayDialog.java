package com.wind.main.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wind.main.R;

/**
 * Created by xiaoxiao on 2017/12/15.
 */

public class WeekDayDialog extends AlertDialog implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private LayoutInflater layoutInflater;
    private Context context;
    private View view;
    private Button mDone;
    private CheckBox mMon ,mTue,mWed,mThu,mFri,mSat,mSun;
    private StringBuilder stringBuilder =new StringBuilder();
    Callback callback;
    public WeekDayDialog(Context context,Callback callback){
        super(context);
        this.context =context;
        this.callback =callback;
        initView();
    }

    public void initView(){
        layoutInflater =LayoutInflater.from(context);
        view =layoutInflater.inflate(R.layout.weekday_picker,null);
        mDone =view.findViewById(R.id.btn_ad_done);
        mMon =view.findViewById(R.id.ad_monday);
        mTue =view.findViewById(R.id.ad_tuesday);
        mWed =view.findViewById(R.id.ad_wednesday);
        mThu =view.findViewById(R.id.ad_thursday);
        mFri =view.findViewById(R.id.ad_friday);
        mSat =view.findViewById(R.id.ad_saturday);
        mSun =view.findViewById(R.id.ad_sunday);
        mMon.setOnCheckedChangeListener(this);
        mTue.setOnCheckedChangeListener(this);
        mWed.setOnCheckedChangeListener(this);
        mThu.setOnCheckedChangeListener(this);
        mFri.setOnCheckedChangeListener(this);
        mSat.setOnCheckedChangeListener(this);
        mSun.setOnCheckedChangeListener(this);
        mDone.setOnClickListener(this);
        setView(view);
    }

    @Override
    public void onClick(View v) {
        callback.updateData();
        dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ad_monday:
                if(isChecked){
                    stringBuilder.append("Mon ");
                }
                break;
            case R.id.ad_tuesday:
                stringBuilder.append("Tue ");
                break;
            case R.id.ad_wednesday:
                stringBuilder.append("Wed ");
                break;
            case R.id.ad_thursday:
                stringBuilder.append("Thur ");
                break;
            case R.id.ad_friday:
                stringBuilder.append("Fri ");
                break;
            case R.id.ad_saturday:
                stringBuilder.append("Sat ");
                break;
            case R.id.ad_sunday:
                stringBuilder.append("Sun ");
                break;
        }
    }
    public interface Callback{
        void updateData();
    }

    public String getPeriod(){
        return stringBuilder.toString();
    }
}
