package com.wind.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.main.R;
import com.wind.main.mode.PriceTimeItem;
import com.wind.main.util.LogUtil;

import java.util.List;

import static com.wind.main.util.EnumUtil.ORDER_STATUS_CHECK;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_DISPLAY;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPDATE;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPLOAD;

/**
 * Created by zhuyuqiang on 17-11-19.
 */

public class PriceTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PriceTimeItem> mItems;
    private Drawable mDisplay = null;
    private Drawable mUpdate = null;
    private Drawable mUpload = null;
    private Drawable mCheck = null;
    public PriceTimeAdapter(Context context, List<PriceTimeItem> items){
        this.mContext = context;
        this.mItems = items;
        mDisplay = mContext.getResources().getDrawable(R.drawable.ad_price_time_display);
        mUpdate = mContext.getResources().getDrawable(R.drawable.ad_price_time_update);
        mUpload = mContext.getResources().getDrawable(R.drawable.ad_price_time_upload);
        mCheck = mContext.getResources().getDrawable(R.drawable.ad_price_time_check);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ad_price_time_item_layout,null);
            holder.data = (TextView)convertView.findViewById(R.id.ad_price_date_text);
            holder.time = (TextView)convertView.findViewById(R.id.ad_price_time_text);
            holder.time_1 = (ImageView) convertView.findViewById(R.id.ad_price_time_1);
            holder.time_2 = (ImageView)convertView.findViewById(R.id.ad_price_time_2);
            holder.time_3 = (ImageView)convertView.findViewById(R.id.ad_price_time_3);
            holder.time_4 = (ImageView)convertView.findViewById(R.id.ad_price_time_4);
            holder.time_5 = (ImageView)convertView.findViewById(R.id.ad_price_time_5);
            holder.time_6 = (ImageView)convertView.findViewById(R.id.ad_price_time_6);
            holder.time_7 = (ImageView)convertView.findViewById(R.id.ad_price_time_7);
            holder.time_8 = (ImageView)convertView.findViewById(R.id.ad_price_time_8);
            holder.time_9 = (ImageView)convertView.findViewById(R.id.ad_price_time_9);
            holder.time_10 = (ImageView)convertView.findViewById(R.id.ad_price_time_10);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        bindView(holder,position);
        return convertView;
    }

    private void bindView(ViewHolder holder,int pos){
        holder.data.setText(mItems.get(pos).getDate());
        holder.time.setText(mItems.get(pos).getTimeStart() + " - " + mItems.get(pos).getTimeStop());
//        holder.time_1.setImageBitmap(getMarkByStatus(mItems.get(pos).getTime_1_status()));
//        holder.time_2.setImageBitmap(getMarkByStatus(mItems.get(pos).getTime_2_status()));
//        holder.time_3.setImageBitmap(getMarkByStatus(mItems.get(pos).getTime_3_status()));
//        holder.time_4.setImageBitmap(getMarkByStatus(mItems.get(pos).getTime_4_status()));
//        holder.time_5.setImageBitmap(getMarkByStatus(mItems.get(pos).getTime_5_status()));
//        holder.time_6.setImageBitmap(getMarkByStatus(mItems.get(pos).getTime_6_status()));
        holder.time_1.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_1_status()));
        holder.time_2.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_2_status()));
        holder.time_3.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_3_status()));
        holder.time_4.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_4_status()));
        holder.time_5.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_5_status()));
        holder.time_6.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_6_status()));
        holder.time_7.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_7_status()));
        holder.time_8.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_8_status()));
        holder.time_9.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_9_status()));
        holder.time_10.setImageDrawable(getMarkByStatus(mItems.get(pos).getTime_10_status()));
    }

//    private Bitmap getMarkByStatus(int status){
//        switch (status){
//            case ORDER_STATUS_DISPLAY:
//                LogUtil("zyq","return mark is display");
//                return BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ad_price_time_display);
//            case ORDER_STATUS_UPLOAD:
//                LogUtil("zyq","return mark is upload");
//                return BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ad_price_time_upload);
//            case ORDER_STATUS_UPDATE:
//                LogUtil("zyq","return mark is update");
//                return BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ad_price_time_update);
//            case ORDER_STATUS_CHECK:
//                LogUtil("zyq","return mark is check");
//                return BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ad_price_time_check);
//        }
//        LogUtil("zyq","return mark is null");
//        return null;
//    }
    private Drawable getMarkByStatus(int status){
        switch (status){
            case ORDER_STATUS_DISPLAY:
                LogUtil.e("zyq", "return mark is display");
                return mDisplay;
            case ORDER_STATUS_UPLOAD:
                LogUtil.e("zyq","return mark is upload");
                return mUpload;
            case ORDER_STATUS_UPDATE:
                LogUtil.e("zyq","return mark is update");
                return mUpdate;
            case ORDER_STATUS_CHECK:
                LogUtil.e("zyq","return mark is check");
                return mCheck;
        }
        LogUtil.e("zyq","return mark is null");
        return null;
    }

    class ViewHolder{
        TextView data,time;
        ImageView time_1,time_2,time_3,time_4,time_5,time_6,time_7,time_8,time_9,time_10;
    }
}
