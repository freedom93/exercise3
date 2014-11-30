package com.freedom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * CommonAdapter 一个抽象类， 除了getView之外的方法都实现了，
 * 故我们的MyAdapter只需要实现一个getView，
 * 然后再getView联合通用的ViewHolder就能打造出万能的Adapter
 */
public class MyAdapter extends CommonAdapter {

	public MyAdapter(Context context, List mData) {
		super(context, mData);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent, R.layout.list_item, position);
		TextView tv = viewHolder.getView(R.id.folder_name);
		tv.setText(mData.get(position).toString());
		return viewHolder.getConvertView();
	}
	
	
	
}
