package com.wind.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wind.main.R;
import com.wind.main.mode.OrderFormItem;

import java.util.List;

import static com.wind.main.util.EnumUtil.ORDER_STATUS_CHECK;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_DISPLAY;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPDATE;
import static com.wind.main.util.EnumUtil.ORDER_STATUS_UPLOAD;

/**
 * Created by zhuyuqiang on 17-11-18.
 */

public class AdDetailListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderFormItem> mItems;

    public AdDetailListAdapter(Context context, List<OrderFormItem> items){
        this.mContext = context;
        this.mItems = items;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ad_detail_item_layout,null);
            holder.orderName = (TextView) convertView.findViewById(R.id.ad_detail_order_name);
            holder.orderDate = (TextView)convertView.findViewById(R.id.ad_detail_order_date);
            holder.orderTime = (TextView)convertView.findViewById(R.id.ad_detail_order_time);
            holder.times = (TextView)convertView.findViewById(R.id.ad_detail_order_count);
            holder.orderStatus = (TextView)convertView.findViewById(R.id.ad_detail_order_status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        bindView(holder,position);
        return convertView;
    }

    private void bindView(ViewHolder holder, int position) {
        holder.orderName.setText(mItems.get(position).getOrderName());
        holder.orderDate.setText(mItems.get(position).getOrderDate());
        holder.orderTime.setText(mItems.get(position).getOrderTime());
        holder.times.setText(mContext.getResources().getString(R.string.ad_detail_order_count,mItems.get(position).getTimes()));
        holder.orderStatus.setText(getOrderStatusMessage(mItems.get(position).getOrderStatus()));
    }

    private String getOrderStatusMessage(int status){
        switch (status){
            case ORDER_STATUS_DISPLAY:
                return mContext.getResources().getString(R.string.ad_order_status_display);

            case ORDER_STATUS_UPLOAD:
                return mContext.getResources().getString(R.string.ad_order_status_upload);

            case ORDER_STATUS_UPDATE:
                return mContext.getResources().getString(R.string.ad_order_status_update);

            case ORDER_STATUS_CHECK:
                return mContext.getResources().getString(R.string.ad_order_status_check);

            default:
                return mContext.getResources().getString(R.string.ad_order_status_display);
        }
    }

    class ViewHolder{
        TextView orderName;
        TextView orderDate;
        TextView orderTime;
        TextView times;
        TextView orderStatus;
    }
}
