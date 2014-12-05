package com.freedom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 通用的Adapter：，
 * Adapter一般需要保持一个List对象，存储一个Bean的集合，
 * 不同的ListView，Bean肯定是不同的，
 * 这个CommonAdapter肯定需要支持泛型，内部维持一个List<T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mData;
	protected final int mItemLayoutId;
	
	public CommonAdapter(Context context, List<T> mData, int itemLayoutId){
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.mContext = context;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {

		return mData.size();
	}

	@Override
	public Object getItem(int position) {

		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		convert(viewHolder, (T) getItem(position));
		return viewHolder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T item);
	
	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent){
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}
	
	
	public void refreshData(List<T> array) {
		this.mData = array;
		notifyDataSetChanged();

	}

}
