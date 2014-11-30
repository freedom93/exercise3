package com.freedom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 通用的Adapter：，
 * Adapter一般需要保持一个List对象，存储一个Bean的集合，
 * 不同的ListView，Bean肯定是不同的，
 * 这个CommonAdapter肯定需要支持泛型，内部维持一个List<T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	
	protected LayoutInflater inflater;
	protected Context context;
	protected List<T> mData;
	
	public CommonAdapter(Context context, List<T> mData){
		inflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	

}
