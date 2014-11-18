package com.freedom.chfolderdb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.freedom.chfolderdb.R;

/**
 * 列表拖动动态重组列表
 * @author lifen
 */
public class CHFolderListInterceptor extends ListView {
	String TAG = "CHFolderListInterceptor";

	private DropListener mDropListener;

	private ImageView mDragView;
	private int mDragPos; // 被拖动Item位置
	private int mFirstDragPos; // 拖动Item的原始位置
	private int mDragPoint; // 拖动Item的相对偏移量
	private int mCoordOffset; // 在View中的屏幕Item拖动前后的偏移

	private Rect mTempRect = new Rect();
	private final int mTouchSlop;
	private int mHeight;
	private int mUpperBound;
	private int mLowerBound;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;
	private int dragndropBackgroundColor = 0x00000000;
	private Bitmap mDragBitmap;
	// 计算依据：文件夹Icon的大小（需要更合理的计算公式！）
	private int mItemHeightHalf = 53;
	private int mItemHeightNormal = 105;
	private int mItemHeightExpanded = 157;

	public CHFolderListInterceptor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CHFolderListInterceptor(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	/**
	 * 触控事件
	 * 
	 * @param ev MotionEvent事件对象
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.v(TAG, "onTouchEvent……");
		if ((mDropListener != null) && mDragView != null) {
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				Rect r = mTempRect;
				mDragView.getDrawingRect(r);
				stopDragging();
				if (mDropListener != null && mDragPos >= 0
						&& mDragPos < getCount()) {
					mDropListener.drop(mFirstDragPos, mDragPos);
				}
				unExpandViews(false);
				break;

			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				dragView(x, y);

				int itemnum = getItemForPosition(y);
				if (itemnum >= 0) {
					if (action == MotionEvent.ACTION_DOWN
							|| itemnum != mDragPos) {
						mDragPos = itemnum;
						doExpansion();
						Log.v(TAG, "doExpansion……");
					}
				}
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mDropListener != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				int itemnum = pointToPosition(x, y);
				Log.v(TAG,"onInterceptTouchEvent itemnum： " + itemnum);
				if (itemnum == AdapterView.INVALID_POSITION) {
					break;
				}
				ViewGroup item = (ViewGroup) getChildAt(itemnum
						- getFirstVisiblePosition());
				Log.v(TAG,"onInterceptTouchEvent itemnum：" + getFirstVisiblePosition() + "---"
								+ ev.getRawY() + "----" + ev.getY() + "-----"
								+ item.getTop());
				mDragPoint = y - item.getTop();
				mCoordOffset = ((int) ev.getRawY()) - y;
				View dragger = item.findViewById(R.id.chfolder_image);
				Rect r = mTempRect;
				// dragger.getDrawingRect(r);

				r.left = dragger.getLeft();
				r.right = dragger.getRight();
				r.top = dragger.getTop();
				r.bottom = dragger.getBottom();

				if ((r.left < x) && (x < r.right)) {
					item.setDrawingCacheEnabled(true);
					// 创建一个副本的绘图缓存，使它不被回收时由框架列表试图清理内存
					Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
					startDragging(bitmap, y);
					mDragPos = itemnum;
					mFirstDragPos = mDragPos;
					mHeight = getHeight();
					int touchSlop = mTouchSlop;
					mUpperBound = Math.min(y - touchSlop, mHeight / 3);
					mLowerBound = Math.max(y + touchSlop, mHeight * 2 / 3);
					return false;
				}
				mDragView = null;
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void startDragging(Bitmap bm, int y) {
		stopDragging();

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP;
		mWindowParams.x = 0;
		mWindowParams.y = y - mDragPoint + mCoordOffset;

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		ImageView v = new ImageView(getContext());
		// int backGroundColor =
		// getContext().getResources().getColor(R.color.black);
		v.setBackgroundColor(dragndropBackgroundColor);
		v.setImageBitmap(bm);
		mDragBitmap = bm;

		mWindowManager = (WindowManager) getContext()
				.getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
	}

	private void stopDragging() {
		if (mDragView != null) {
			WindowManager wm = (WindowManager) getContext().getSystemService(
					"window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}

	private void dragView(int x, int y) {
		float alpha = 1.0f;
		mWindowParams.alpha = alpha;
		mWindowParams.y = y - mDragPoint + mCoordOffset;
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}

	private int getItemForPosition(int y) {
		int adjustedy = y - mDragPoint - mItemHeightHalf;
		int pos = myPointToPosition(0, adjustedy);
		if (pos >= 0) {
			if (pos <= mFirstDragPos) {
				pos += 1;
			}
		} else if (adjustedy < 0) {
			pos = 0;
		}
		return pos;
	}

	private void adjustScrollBounds(int y) {
		if (y >= mHeight / 3) {
			mUpperBound = mHeight / 3;
		}
		if (y <= mHeight * 2 / 3) {
			mLowerBound = mHeight * 2 / 3;
		}
	}

	/**
	 * 恢复所有的ListItem的大小和设置可见
	 * @param deletion
	 */
	private void unExpandViews(boolean deletion) {
		for (int i = 0;; i++) {
			View v = getChildAt(i);
			if (v == null) {
				if (deletion) {
					// 强制更新列表position
					int position = getFirstVisiblePosition();
					int y = getChildAt(0).getTop();
					setAdapter(getAdapter());
					setSelectionFromTop(position, y);
				}
				layoutChildren();
				v = getChildAt(i);
				if (v == null) {
					break;
				}
			}
			ViewGroup.LayoutParams params = v.getLayoutParams();
			params.height = mItemHeightNormal;
			v.setLayoutParams(params);
			v.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 调整Item大小和设置可见，将Item拖动到周围或其他的地方： 删除Item会导致它仍然在同一个地方，然后根据Item的大小正常拖动。
	 * 如果拖Item还是在屏幕上，则使它尽可能的小和扩展项下面的插入点。 如果拖动Item不在屏幕上，只有扩大项目低于当前插入点。
	 */
	private void doExpansion() {
		int childnum = mDragPos - getFirstVisiblePosition();
		if (mDragPos > mFirstDragPos) {
			childnum++;
		}

		View first = getChildAt(mFirstDragPos - getFirstVisiblePosition());

		for (int i = 0;; i++) {
			View vv = getChildAt(i);
			if (vv == null) {
				break;
			}
			int height = mItemHeightNormal;
			int visibility = View.VISIBLE;
			if (vv.equals(first)) {
				// 处理被拖动Item
				if (mDragPos == mFirstDragPos) {
					// 还在原来的位置上
					visibility = View.INVISIBLE;
				} else {
					// 不是在原来的位置上
					height = 1;
				}
			} else if (i == childnum) {
				if (mDragPos < getCount() - 1) {
					height = mItemHeightExpanded;
				}
			}
			ViewGroup.LayoutParams params = vv.getLayoutParams();
			params.height = height;
			vv.setLayoutParams(params);
			vv.setVisibility(visibility);
		}
	}

	/**
	 * 获取点的位置（考虑不可见的View）
	 * @param x 触摸的中间区域在屏幕的X轴
	 * @param y 触摸的中间区域在屏幕的Y轴
	 * @return
	 */
	private int myPointToPosition(int x, int y) {
		Rect frame = mTempRect;
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			final View child = getChildAt(i);
			child.getHitRect(frame);
			if (frame.contains(x, y)) {
				return getFirstVisiblePosition() + i;
			}
		}
		return INVALID_POSITION;
	}

	public interface DropListener {
		void drop(int from, int to);
	}

	public void setDropListener(DropListener onDrop) {
		mDropListener = onDrop;
	}

}
