package com.wind.main.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wind.main.R;

/**
 * Created by xiaoxiao on 2017/12/12.
 */

public class TimePickerDialog extends AlertDialog implements TimePicker.OnTimeChangedListener,View.OnClickListener{

    private  Context context;
    private View view;
    TimePicker mTimePicker1,mTimePicker2;
    private  TextView mTextView1,mTextView2;
    String tv_open_time ="18:00",tv_close_time ="20:00";
    public  String open_close_time ="18:00-20:00";
    private Button btn_confirm,btn_cancel;
    DialogState dialogState;
    //View.OnClickListener mOnClickListener;
    public TimePickerDialog(Context context,DialogState dialogState){
        super(context);
        this.context =context;
        this.dialogState =dialogState;
        view =LayoutInflater.from(context).inflate(R.layout.ad_item_time_picker,null);
        mTimePicker1 =view.findViewById(R.id.time_picker1);
        mTimePicker2 =view.findViewById(R.id.time_picker2);
        mTextView1 =(TextView)view.findViewById(R.id.tv_start_time);
        mTextView2 =(TextView)view.findViewById(R.id.tv_end_time);
        btn_confirm =(Button)view.findViewById(R.id.btn_confirm);
        btn_cancel =(Button)view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        Log.d("xx/dialog","m1: "+mTextView1);
        mTimePicker1.setOnTimeChangedListener(this);
        mTimePicker2.setOnTimeChangedListener(this);
        mTimePicker1.setIs24HourView(true);
        mTimePicker2.setIs24HourView(true);
        initData();
    }

    public void initData(){
        Log.d("xx/dialog","m1; " +mTextView1);
        mTextView1.setText(context.getResources().getText(R.string.open_time) +(mTimePicker1.getHour()<9 ? ("0"+mTimePicker1.getHour()):mTimePicker1.getHour()+":"
        +(mTimePicker1.getMinute()< 9 ?("0"+mTimePicker1.getMinute()):mTimePicker1.getMinute())));
        mTextView2.setText(context.getResources().getText(R.string.close_time) + (mTimePicker2.getHour()<9 ? ("0"+mTimePicker2.getHour()):mTimePicker2.getHour()+":"
                +(mTimePicker2.getMinute()< 9 ?("0"+mTimePicker2.getMinute()):mTimePicker2.getMinute())));
        setView(view);
    }

    public String getTime(){
        String open_time =(mTimePicker1.getHour()<9 ? ("0"+mTimePicker1.getHour()):mTimePicker1.getHour()+":"
                +(mTimePicker1.getMinute()< 9 ?("0"+mTimePicker1.getMinute()):mTimePicker1.getMinute()));//mTimePicker1.getHour() +":"+mTimePicker1.getMinute();
        String closed_time =(mTimePicker2.getHour()<9 ? ("0"+mTimePicker2.getHour()):mTimePicker2.getHour()+":"
                +(mTimePicker2.getMinute()< 9 ?("0"+mTimePicker2.getMinute()):mTimePicker2.getMinute()));//mTimePicker2.getHour() +":"+mTimePicker2.getMinute();
        open_close_time =open_time +"-"+closed_time;
        return open_close_time;
    }

    public void dismissDialog(){
        dismiss();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        int id =view.getId();
        StringBuilder stringBuilder =new StringBuilder();
        switch (id){
            case R.id.time_picker1:
                if(hourOfDay<10){
                    stringBuilder.append("0").append(hourOfDay);
                }else{
                    stringBuilder.append(hourOfDay);
                }
                stringBuilder.append(":");
                if(minute<10){
                    stringBuilder.append("0").append(minute);
                }else{
                    stringBuilder.append(minute);
                }
                tv_open_time =stringBuilder.toString();
                break;
            case R.id.time_picker2:
                if(hourOfDay<10){
                    stringBuilder.append("0").append(hourOfDay);
                }else{
                    stringBuilder.append(hourOfDay);
                }
                stringBuilder.append(":");
                if(minute<10){
                    stringBuilder.append("0").append(minute);
                }else{
                    stringBuilder.append(minute);
                }
                tv_close_time =stringBuilder.toString();
                break;
            default:
                break;
        }
        initData();
    }

//    public void setListerner(View.OnClickListener onClickListener){
//        mOnClickListener =onClickListener;
//    }
    public interface DialogState{
        void cancelDialog();
        void confirmDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                dialogState.confirmDialog();
                break;
            case R.id.btn_cancel:
                dialogState.cancelDialog();
                break;
        }
    }
}
