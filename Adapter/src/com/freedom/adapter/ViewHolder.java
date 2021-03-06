package com.freedom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 通用的ViewHolder
 * ViewHolder的作用:通过convertView.setTag与convertView进行绑定，
 * 然后当convertView复用时，直接从与之对于的ViewHolder(getTag)中拿到convertView布局中的控件，省去了findViewById的时间~
 * 也就是说，实际上们每个convertView会绑定一个ViewHolder对象，这个viewHolder主要用于帮convertView存储布局中的控件。
 * 写出一个通用的ViewHolder，然后对于任意的convertView，提供一个对象让其setTag即可。
 * 针对不同的Item布局，提供一个容器，专门存每个Item布局中的所有控件，而且还要能够查找出来；
 * 既然需要查找，那么ListView肯定是不行了，需要一个键值对进行保存，键为控件的Id，值为控件的引用，相信大家立刻就能想到Map；
 * 但是我们不用Map，因为有更好的替代类，就是我们android提供的SparseArray这个类，
 * 和Map类似，但是比Map效率，不过键只能为Integer.
 */
public class ViewHolder {
	
	/** 
     * 使用SparseArray<View>用于存储与之对于的convertView的所有的控件，
     * 当需要拿这些控件时，通过getView(id)进行获取；
     * SparseArray和Map类似，但比Map有效率，键值只能是Integer
     */  
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	
	/**
	 * ViewHolder构造函数
	 * @param context
	 * @param parent
	 * @param layoutId
	 * @param position
	 */
	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position){
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		//setTag
		mConvertView.setTag(this);
	}
	
	/** 
     * 拿到一个ViewHolder对象 
     * @param context 
     * @param convertView 
     * @param parent 
     * @param layoutId 
     * @param position 
     * @return 
     */  
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		if(convertView == null){
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}
	
	 /** 
     * 通过控件的Id获取对于的控件，如果没有则加入views 
     * @param viewId 
     * @return 
     */  
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
			
		}
		return (T) view;
	}
	
	public View getConvertView(){
		return mConvertView;
	}
	
	//UpGradeViewHolder
	//其实布局里面的View常用也就那么几种：ImageView,TextView,Button,CheckBox等等
	//ViewHolder还可以封装一些常用的方法，
	//比如setText(id,String)；setImageResource(viewId, resId)；setImageBitmap(viewId, bitmap)；

	/**
	 * 为TextView设置字符串
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text){
		TextView view  = getView(viewId);
		view.setText(text);
		return this;
	}
	
	/** 
     * 为ImageView设置图片 
     * @param viewId 
     * @param resId 
     * @return 
     */  
	public ViewHolder setImageResource(int viewId, int resId){
		ImageView view  = getView(viewId);
		view.setImageResource(resId);
		return this;
	}
	
	/**
	 * 为ImageView设置图片 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm){
		ImageView view  = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}
	
	/**
	 * 通过Url为ImageView设置图片 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, String url){
		//待实现
		return this;
	}
	
	
	
}
