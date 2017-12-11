package com.wind.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wind.main.R;
import com.wind.main.mode.CircleTimeItem;
import com.wind.main.util.LogUtil;

import java.util.List;

/**
 * Created by zhuyuqiang on 17-11-24.
 */

public class CirCleTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<CircleTimeItem> mItems;
    public CirCleTimeAdapter(Context context, List<CircleTimeItem> items){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.circle_time_item_layout,null);
            holder = new ViewHolder();
            holder.startEndTime = (TextView) convertView.findViewById(R.id.ad_start_end_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.startEndTime.setText(mItems.get(position).getStartTime()+"\n"+mItems.get(position).getEndTime());
        return convertView;
    }

    class ViewHolder {
        TextView startEndTime;
    }
}
