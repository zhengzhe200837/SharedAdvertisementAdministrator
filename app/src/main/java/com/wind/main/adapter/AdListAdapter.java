package com.wind.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.main.R;
import com.wind.main.mode.AdListItem;

import java.util.List;

/**
 * Created by zhuyuqiang on 17-11-17.
 */

public class AdListAdapter extends BaseAdapter {

    private Context mContext;
    private List<AdListItem> mAdListItems;

    public AdListAdapter(Context context, List<AdListItem> adListItems) {
        this.mContext = context;
        this.mAdListItems = adListItems;
    }

    public void setData(List<AdListItem> data) {
        mAdListItems = data;
    }

    @Override
    public int getCount() {
        return mAdListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdListItems.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ad_list_item_layout,null);
            holder.adListItemIcon = (ImageView) convertView.findViewById(R.id.ad_list_item_icon);
            holder.adListItemName = (TextView) convertView.findViewById(R.id.ad_list_item_name);
            holder.adListItemCount = (TextView)convertView.findViewById(R.id.ad_list_item_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        bindView(holder,position);
        return convertView;
    }

    private void bindView(ViewHolder holder,int pos){
        holder.adListItemIcon.setImageBitmap(mAdListItems.get(pos).getIcon());
        holder.adListItemName.setText(mAdListItems.get(pos).getAdDevice());
        holder.adListItemCount.setText(mContext.getResources().getString(R.string.order_count,mAdListItems.get(pos).getCount()));
    }

    class ViewHolder {
        TextView adListItemCount;
        TextView adListItemName;
        ImageView adListItemIcon;
    }
}
