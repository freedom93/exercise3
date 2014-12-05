package com.freedom.adapter;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RefleshListView extends ListView implements OnScrollListener{

	public interface IListViewState {
		int LVS_NORMAL = 0; // 普通状态
		int LVS_PULL_REFRESH = 1; // 下拉刷新状态
		int LVS_RELEASE_REFRESH = 2; // 松开刷新状态
		int LVS_LOADING = 3; // 加载状态
	}

	public interface IOnRefreshListener {
		void OnRefresh();
	}

	private View mHeadView;
	private TextView mRefreshTextview;
	private TextView mLastUpdateTextView;
	private ImageView mImageView;

	private int mHeadContentWidth;
	private int mHeadContentHeight;

	private IOnRefreshListener mOnRefreshListener; // 头部刷新监听器

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecord = false;
	// 标记的Y坐标值
	private int mStartY = 0;
	// 当前视图能看到的第一个项的索引
	private int mFirstItemIndex = -1;
	// MOVE时保存的Y坐标值
	private int mMoveY = 0;
	// LISTVIEW状态
	private int mViewState = IListViewState.LVS_NORMAL;

	private final static int RATIO = 2;

	private RotateAnimation animation;
	private RotateAnimation loadAnimation;
	private RotateAnimation reverseAnimation;
	private boolean mBack = false;

	public RefleshListView(Context context) {
		super(context);
		init(context);
	}

	public RefleshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setOnRefreshListener(IOnRefreshListener listener) {
		mOnRefreshListener = listener;
	}

	private void onRefresh() {
		if (mOnRefreshListener != null) {
			mOnRefreshListener.OnRefresh();
		}
	}

	public void onRefreshComplete() {
		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mLastUpdateTextView.setText("最近更新:" + new Date().toLocaleString());
		switchViewState(IListViewState.LVS_NORMAL);
	}

	private void init(Context context) {
		initHeadView(context);

		setOnScrollListener(this);
	}

	// 初始化headview试图
	private void initHeadView(Context context) {
		mHeadView = LayoutInflater.from(context).inflate(R.layout.refresh_list_head, null);

		mImageView = (ImageView) mHeadView.findViewById(R.id.icon_reflesh);
		mImageView.setMinimumWidth(60);

		mRefreshTextview = (TextView) mHeadView.findViewById(R.id.head_reflesh_tips);

		mLastUpdateTextView = (TextView) mHeadView.findViewById(R.id.head_reflesh_lastUpdated);

		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight();
		mHeadContentWidth = mHeadView.getMeasuredWidth();

		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mHeadView.invalidate();

		addHeaderView(mHeadView, null, false);

		animation = new RotateAnimation(0, 540,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(1000);
		animation.setFillAfter(true);

		loadAnimation = new RotateAnimation(540, 1620,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		loadAnimation.setInterpolator(new LinearInterpolator());
		loadAnimation.setDuration(2000);
		loadAnimation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}

	// 计算headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisiableItem;

	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (mOnRefreshListener != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				doActionDown(ev);
				break;
			case MotionEvent.ACTION_MOVE:
				doActionMove(ev);
				break;
			case MotionEvent.ACTION_UP:
				doActionUp(ev);
				break;
			default:
				break;
			}
		}

		return super.onTouchEvent(ev);
	}

	private void doActionDown(MotionEvent ev) {
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}
	}

	private void doActionMove(MotionEvent ev) {
		mMoveY = (int) ev.getY();

		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}

		if (mIsRecord == false || mViewState == IListViewState.LVS_LOADING) {
			return;
		}

		int offset = (mMoveY - mStartY) / RATIO;

		switch (mViewState) {
		case IListViewState.LVS_NORMAL: {
			if (offset > 0) {
				mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
				switchViewState(IListViewState.LVS_PULL_REFRESH);
			}
		}
			break;
		case IListViewState.LVS_PULL_REFRESH: {
			setSelection(0);
			mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
			if (offset < 0) {
				switchViewState(IListViewState.LVS_NORMAL);
			} else if (offset > mHeadContentHeight) {
				switchViewState(IListViewState.LVS_RELEASE_REFRESH);
			}
		}
			break;
		case IListViewState.LVS_RELEASE_REFRESH: {
			setSelection(0);
			mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
			if (offset >= 0 && offset <= mHeadContentHeight) {
				mBack = true;
				switchViewState(IListViewState.LVS_PULL_REFRESH);
			} else if (offset < 0) {
				switchViewState(IListViewState.LVS_NORMAL);
			} else {

			}

		}
			break;
		default:
			return;
		}
		;

	}

	private void doActionUp(MotionEvent ev) {
		mIsRecord = false;
		mBack = false;

		if (mViewState == IListViewState.LVS_LOADING) {
			return;
		}

		switch (mViewState) {
		case IListViewState.LVS_NORMAL:

			break;
		case IListViewState.LVS_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
			switchViewState(IListViewState.LVS_NORMAL);
			break;
		case IListViewState.LVS_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(IListViewState.LVS_LOADING);
			onRefresh();
			break;
		}

	}

	// 切换headview视图
	private void switchViewState(int state) {

		switch (state) {
		case IListViewState.LVS_NORMAL: {
			Log.i("test", "convert to IListViewState.LVS_NORMAL");
			mImageView.clearAnimation();
			mImageView.setImageResource(R.drawable.reflesh_48);
		}
			break;
		case IListViewState.LVS_PULL_REFRESH: {
			Log.i("test", "convert to IListViewState.LVS_PULL_REFRESH");
			mImageView.setVisibility(View.VISIBLE);
			mRefreshTextview.setText("下拉可以刷新");
			mImageView.clearAnimation();

			// 是由RELEASE_To_REFRESH状态转变来的
			if (mBack) {
				mBack = false;
				mImageView.clearAnimation();
				mImageView.startAnimation(reverseAnimation);
			}
		}
			break;
		case IListViewState.LVS_RELEASE_REFRESH: {
			Log.i("test","convert to IListViewState.LVS_RELEASE_REFRESH");
			mImageView.setVisibility(View.VISIBLE);
			mRefreshTextview.setText("松开加载更多");
			mImageView.clearAnimation();
			mImageView.startAnimation(animation);
		}
			break;
		case IListViewState.LVS_LOADING: {
			Log.i("test", "convert to IListViewState.LVS_LOADING");
			mRefreshTextview.setText("努力加载中...");
			mImageView.startAnimation(loadAnimation);

		}
			break;
		default:
			return;
		}

		mViewState = state;

	}

}
