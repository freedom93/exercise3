package com.freedom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> mDatas;
	
	public MyAdapter(Context context, List<String> mDatas){
		
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = mDatas;
	}
	
	

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//实例化一个viewHolder
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.list_item, position);
		//通过getView获取控件
		TextView tv = holder.getView(R.id.folder_name);
		//使用
		tv.setTag(mDatas.get(position));
		return holder.getConvertView();
	}
	
}
