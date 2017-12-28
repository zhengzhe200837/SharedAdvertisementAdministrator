package com.wind.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.wind.main.dialog.TimePickerDialog;
import com.wind.main.dialog.WeekDayDialog;
import com.wind.main.network.model.BillboardInfo;
import com.wind.main.util.LogUtil;

import java.util.Calendar;


/**
 * Created by xiaoxiao on 2017/12/15.
 */

public class AdAddItemActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mShowTime;
    private TextView mOpenDate;
    private TextView mCloseDate;
    private TextView mOpenTime;
    private TextView mCloseTime;
    private EditText mAdCharge;
    private EditText mAdUnite;
    private TextView mAdDetailAddress;
    private EditText mAdName;
    private EditText mAdPhoneNumber;
    private Button mConfirm;
    private Button mCancel;
    private BillboardInfo billboardInfo;
  //  private TimePickerDialog timePickerDialog;
    private WeekDayDialog weekDayDialog;
    private Context context;
    private AlertDialog mDetailAddr;
    private static final int MAP_REQUEST =10;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private Double latitude;
    private Double longitude;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(R.style.add_ad_Theme);
        setContentView(R.layout.ad_detail_item);
        initView();
    }

    public void initView(){
        context =this;
        mShowTime =(TextView)findViewById(R.id.tv_show_time);
        mOpenDate =(TextView)findViewById(R.id.tv_open_date);
        mCloseDate =(TextView)findViewById(R.id.tv_close_date);
        mOpenTime =(TextView)findViewById(R.id.tv_open_time);
        mCloseTime =(TextView)findViewById(R.id.tv_close_time);
        mAdCharge =(EditText) findViewById(R.id.tv_charge);
        mAdUnite =(EditText)findViewById(R.id.tv_unit);
        mAdDetailAddress =(TextView)findViewById(R.id.tv_detail_address);
        mAdName =(EditText)findViewById(R.id.tv_ad_name);
        mAdPhoneNumber =(EditText)findViewById(R.id.tv_ad_phone_number);
        mConfirm =(Button)findViewById(R.id.btn_ad_confirm);
        mCancel =(Button)findViewById(R.id.btn_ad_cancel);
        billboardInfo =new BillboardInfo();
        applyListener();



    }

    public void applyListener(){
        mOpenDate.setOnClickListener(this);
        mCloseDate.setOnClickListener(this);
        mOpenTime.setOnClickListener(this);
        mCloseTime.setOnClickListener(this);
      //  mAdCharge.setOnClickListener(this);
      //  mAdUnite.setOnClickListener(this);
        mAdDetailAddress.setOnClickListener(this);
       // mAdName.setOnClickListener(this);
       // mAdPhoneNumber.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_open_date:
                Calendar calendarOpen =Calendar.getInstance();
                int year =calendarOpen.get(Calendar.YEAR);
                int month =calendarOpen.get(Calendar.MONTH);
                int day =calendarOpen.get(Calendar.DAY_OF_MONTH);
                 datePickerDialog =new DatePickerDialog(AdAddItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     mOpenDate.setText(""+year+""+(month <9 ?("0"+(month+1)) : (month+1)) +""+(dayOfMonth<10 ?("0"+dayOfMonth) :dayOfMonth));
                    }
                },year,month,day);
                datePickerDialog.show();
                break;
            case R.id.tv_close_date:
                Calendar calendarClose =Calendar.getInstance();
                int year1 =calendarClose.get(Calendar.YEAR);
                int month1 =calendarClose.get(Calendar.MONTH) ;
                int day1 =calendarClose.get(Calendar.DAY_OF_MONTH);
                datePickerDialog =new DatePickerDialog(AdAddItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCloseDate.setText(""+year+""+(month <9 ?("0"+(month+1)) : (month+1)) +""+(dayOfMonth<10 ?("0"+dayOfMonth) :dayOfMonth));
                    }
                },year1,month1,day1);
                datePickerDialog.show();
                break;
            case R.id.tv_open_time:
                Calendar calendarOpenTime =Calendar.getInstance();
                int hour =calendarOpenTime.get(Calendar.HOUR_OF_DAY);
                int minute =calendarOpenTime.get(Calendar.MINUTE);
                mTimePickerDialog =new TimePickerDialog(AdAddItemActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mOpenTime.setText(""+(hourOfDay<10 ? ("0"+hourOfDay):hourOfDay)+""+(minute<10 ?("0"+minute):minute) +"00");
                    }
                },hour,minute,true);
                mTimePickerDialog.show();
//            Intent intent1 =new Intent(this,TimePickerActivity.class);
//                startActivity(intent1);
//                     timePickerDialog = new TimePickerDialog(AdAddItemActivity.this, new TimePickerDialog.DialogState() {
//                        @Override
//                        public void cancelDialog() {
//                            timePickerDialog.dismissDialog();
//                        }
//                        @Override
//                        public void confirmDialog() {
//                             mOpenTime.setText(timePickerDialog.getTime());
//                            timePickerDialog.dismissDialog();
//                        }
//                    });
//                    timePickerDialog.show();
                break;
            case R.id.tv_close_time:
                Calendar calendarCloseTime =Calendar.getInstance();
                int hour1 =calendarCloseTime.get(Calendar.HOUR_OF_DAY);
                int minute1 =calendarCloseTime.get(Calendar.MINUTE);
                mTimePickerDialog =new TimePickerDialog(AdAddItemActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mCloseTime.setText(""+(hourOfDay<10 ? ("0"+hourOfDay):hourOfDay)+""+(minute<10 ?("0"+minute):minute)+"00");
                    }
                },hour1,minute1,true);
                mTimePickerDialog.show();

//                weekDayDialog =new WeekDayDialog(context, new WeekDayDialog.Callback() {
//                    @Override
//                    public void updateData() {
//                        mShowPeriodTime.setText(weekDayDialog.getPeriod());
//                    }
//                });
//                weekDayDialog.show();
                break;
            case R.id.tv_unit:
               // inputDetailAddr("时间单位",mAdUnite);
                break;
            case R.id.tv_detail_address:
                Intent intentMap =new Intent();
                intentMap.setClass(this,MapActivity.class);
                startActivityForResult(intentMap,MAP_REQUEST);
                break;
            case R.id.tv_ad_name:
                //inputDetailAddr("设备名称",mAdName);
                break;
            case R.id.tv_ad_phone_number:
              //  inputDetailAddr("联系电话",mAdPhoneNumber);
                break;
            case R.id.tv_charge:
              //  inputDetailAddr("收费标准",mAdCharge);
                break;
            case R.id.btn_ad_confirm:
                if(!isNotEmpty()) {
                    Toast.makeText(context,"字段不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent =getIntent();
                String nItemName = mShowTime.getText().toString();
                intent.putExtra("item_name",nItemName);
                billboardInfo.setEndDate(mCloseDate.getText().toString());
                billboardInfo.setStartDate(mOpenDate.getText().toString());
                billboardInfo.setStartTime(mOpenTime.getText().toString());
                billboardInfo.setEndTime(mCloseTime.getText().toString());
                billboardInfo.setAddress(mAdDetailAddress.getText().toString());
                billboardInfo.setBusinessPhone(mAdPhoneNumber.getText().toString());
                billboardInfo.setPrice(Integer.parseInt(mAdCharge.getText().toString()));
                //billboardInfo.setEquipmentType(mAdName.getText().toString());
                billboardInfo.setEquipmentName(mAdName.getText().toString());
                billboardInfo.setLongitude(longitude);
                billboardInfo.setLatitude(latitude);
                intent.putExtra("billboardinfo",billboardInfo);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.btn_ad_cancel:
                finish();
                break;
            default:
                break;

        }
    }

    private void inputDetailAddr(String text, final TextView textView){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(com.wind.main.R.layout.add_ad_modify_image_name,null);
        TextView title = (TextView) view.findViewById(com.wind.main.R.id.add_ad_image_title);
        title.setText(text);
        final EditText editor = (EditText) view.findViewById(com.wind.main.R.id.add_ad_image_editor);
        Button cancel = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDetailAddr != null){
                    mDetailAddr.dismiss();
                }
            }
        });
        Button ensure = (Button) view.findViewById(com.wind.main.R.id.add_ad_image_ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDetailAddr != null){
                    String detailAddr = editor.getText().toString();
                    if(!TextUtils.isEmpty(detailAddr)){
                        textView.setText(detailAddr);
                        mDetailAddr.dismiss();
                    }
                }
            }
        });
        mBuilder.setView(view);
        mBuilder.setCancelable(true);
        mDetailAddr = mBuilder.create();
        mDetailAddr.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==MAP_REQUEST && resultCode ==RESULT_OK){
            String address1 =data.getStringExtra("address");
            latitude =data.getDoubleExtra("latitude",0);
            longitude =data.getDoubleExtra("longitude",0);
            LogUtil.d("address1: "+address1 +data.getStringExtra("dddd"));
            if(address1 !=null)
            mAdDetailAddress.setText(address1);
        }
    }

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(mShowTime.getText()) || TextUtils.isEmpty(mOpenDate.getText()) || TextUtils.isEmpty(mCloseDate.getText())
                || TextUtils.isEmpty(mOpenTime.getText()) || TextUtils.isEmpty(mCloseTime.getText()) ||
                TextUtils.isEmpty(mAdCharge.getText())|| TextUtils.isEmpty(mAdUnite.getText())|| TextUtils.isEmpty(mAdDetailAddress.getText())
                || TextUtils.isEmpty(mAdName.getText())|| TextUtils.isEmpty(mAdPhoneNumber.getText())){
            return false;
        }
        else return true;
    }
}
