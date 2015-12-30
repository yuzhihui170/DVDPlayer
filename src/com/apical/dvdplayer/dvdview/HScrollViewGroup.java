package com.apical.dvdplayer.dvdview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 水平滑动或翻页，目前只支持水平滑动 2014.03.17
 * 
 * @author Administrator
 * 
 */
public class HScrollViewGroup extends ViewGroup {

	private static final String TAG = "HScrollViewGroup_dzt";
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 50;// 滑动视图的速率
	private static final int INTERVAL = 8; // 每次滑动的间隔

	private Scroller mScroller; // 滑动控件
	private VelocityTracker mVelocityTracker; // 速度追踪器
	private Direction mDirection = Direction.NONE;

	private int mCurScreen; // 记录当前页
	private int mDefaultScreen = 0; // 默认页
	private int mTouchState = TOUCH_STATE_REST;// 设置触发状态
	private int mTouchSlop; // 触发移动的像素距离
	private float mLastMotionX; // 手指触碰屏幕的最后一次x坐标
	private float mLastMotionY; // 手指触碰屏幕的最后一次y坐标
	private float mDownMotionX; // 按下手势的X坐标
	private float mUpMotionX; // 松手的X坐标
	private boolean mMotionFinish = true; // 一个滑动动作完成
	private int mTotalPage; // 总页数
	private int mMaxWidth; // 所有子控件加起来的总宽度
	private int mWidth; // 每个子控件的宽度
	private int mCtrlWidth = 0;
	private int mRemainder; // 总宽度除以每页的余数
	private int mMoveCount; // 移动计数器
	int[] mScreens = new int[5];// 每页的最前一个坐标
	
	private int snapTime = 1500;  //用滑动动画时间来控件速度
	
	private Context mContext;

	public HScrollViewGroup(Context context) {
		super(context);
		this.mContext=context;
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HScrollViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.mContext=context;
		// TODO Auto-generated constructor stub
	}

	public HScrollViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext=context;
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		mScroller = new Scroller(context);
		mCurScreen = mDefaultScreen;// 默认设置显示第一个VIEW
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		Log.d(TAG, "init---mTouchSlop = " + mTouchSlop);
	}

	/**
	 * 父类为子类在屏幕上分配实际的宽度和高度,里面的四个参数分别表示，布局是否发生改变，布局左 上右下的边距
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLayout changed = " + changed);
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();

			Log.d(TAG, "childCount = " + childCount);
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					if (childCount > 5
							&& ((i != 0) && ((i % 4 == 0)) || (i == childCount - 1))) {
						childView.layout(childLeft, 0, childLeft
								+ (mWidth + mRemainder),
								childView.getMeasuredHeight());
						childLeft += (mWidth + mRemainder);
					} else {
						childView.layout(childLeft, 0, childLeft + mWidth,
								childView.getMeasuredHeight());
						childLeft += mWidth;
					}

					Log.d(TAG, "childLeft=" + childLeft + " childWidth="
							+ mWidth);
				}
			}
			calculateScreens();
		}
	}

	/**
	 * 计算每页第一个item的位置
	 */
	void calculateScreens() {
		int childLeft = 0;
		int viewWidth = getWidth();

		int curPage = 0;
		mScreens[curPage] = childLeft;
		++curPage;

		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				if (childCount > 5
						&& ((i != 0) && ((i % 4 == 0)) || (i == childCount - 1))) {
					childLeft += (mWidth + mRemainder);
					if (childLeft > (viewWidth)) {
						mScreens[curPage] = childLeft - (mWidth + mRemainder)
								+ mScreens[curPage - 1];
						++curPage;
						childLeft = (mWidth + mRemainder);
					}
				} else {
					childLeft += mWidth;
					if (childLeft > (viewWidth)) {
						mScreens[curPage] = childLeft - mWidth
								+ mScreens[curPage - 1];
						++curPage;
						childLeft = mWidth;
					}
				}

			}
			Log.d(TAG, "childLeft = " + childLeft);
		}

		if (childLeft != 0 && curPage > 1) {
			mScreens[curPage - 1] = mScreens[curPage - 1] + childLeft
					- viewWidth;
		}

		for (int screen : mScreens) {
			Log.d(TAG, " screen = " + screen);
		}
	}

	/**
	 * MeasureSpec类的静态方法getMode和getSize来译解。一个MeasureSpec包含一个尺寸和模式。
	 * 
	 * 有三种可能的模式：
	 * 
	 * UNSPECIFIED：父布局没有给子布局任何限制，子布局可以任意大小。
	 * EXACTLY：父布局决定子布局的确切大小。不论子布局多大，它都必须限制在这个界限里
	 * 。(当布局定义为一个固定像素或者fill_parent时就是EXACTLY模式)
	 * AT_MOST：子布局可以根据自己的大小选择任意大小。(当布局定义为wrap_content时就是AT_MOST模式)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);
		if (mCtrlWidth != width) {
			mCtrlWidth = width;
			mWidth = width / 5;
			mRemainder = width % 5;

			// The children are given the same width and height as the
			// scrollLayout
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (count > 5
						&& ((i != 0) && ((i % 4 == 0)) || (i == count - 1))) {
					getChildAt(i).measure((mWidth + mRemainder),
							heightMeasureSpec);
				} else {
					getChildAt(i).measure(mWidth, heightMeasureSpec);
				}
			}
			mMaxWidth = (getChildCount() * mWidth) + mRemainder;
			//mTotalPage = mMaxWidth / width;
			mTotalPage = getChildCount()/5 + (getChildCount()%5 > 0 ? 1 : 0);
			snapToScreen(mCurScreen);
			mScroller.abortAnimation();
			Log.d(TAG, "mTotalPage = " + mTotalPage + " width = " + width
					+ " height = " + height + " count = " + count
					+ " mCurScreen = " + mCurScreen);
		}
	}

	/**
	 * 根据滑动的距离判断移动到第几个视图
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int scrollX = getScrollX() > mMaxWidth ? mMaxWidth : getScrollX();
		final int destScreen = (scrollX + screenWidth / 2) / screenWidth;
		Log.d(TAG, "screenWidth = " + screenWidth + " destScreen = "
				+ destScreen + " scrollx = " + scrollX);
		snapToScreen(destScreen);
	}

	/**
	 * 滚动到制定的视图
	 * 
	 * @param whichScreen
	 *            视图下标
	 */
	public void snapToScreen(int whichScreen) 
	{
		//whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		//if (getScrollX() != (whichScreen * getWidth())) 
		{

			// final int delta = whichScreen * getWidth() - getScrollX();
			final int delta = mScreens[whichScreen] - getScrollX();
			Log.d(TAG, "snapToScreen-whichScreen = " + whichScreen
					+ " delta = " + delta + " scrollX = " + getScrollX());
			mScroller.startScroll(getScrollX(), 0, delta, 0, snapTime);
			mCurScreen = whichScreen;
			mMoveCount = getScrollX();
			invalidate();
		}
	}

	/**
	 * 设置当前滑动方向
	 * 
	 * @param dir
	 */
	public void setDirection(Direction dir) {
		mDirection = dir;
	}

	/**
	 * 获取当前滑动方向
	 * 
	 * @return
	 */
	public Direction getDirection() {
		return mDirection;
	}

	/**
	 * 获取当前页
	 * 
	 * @return
	 */
	public int getCurScreen() {
		return mCurScreen;
	}

	/**
	 * 获取总页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		return mTotalPage;
	}

	/*
	 * 当我们执行ontouch或invalidate(）或postInvalidate()都会导致这个方法的执行*/
	@Override
	public void computeScroll() 
	{
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
//			if (mDirection == Direction.LEFT) {
				// Log.d(TAG, "left mScreens[mCurScreen] = "
				// + mScreens[mCurScreen]);
//				mMoveCount -=INTERVAL ;
//				if (mMoveCount < mScreens[mCurScreen]) {
//					mMoveCount = mScreens[mCurScreen];
//					mScroller.abortAnimation();
//				}
//				scrollTo(mMoveCount, mScroller.getCurrY());
				//scrollTo(mScreens[0], mScroller.getCurrY());
			Log.d(TAG, "mScroller.getCurrX() = " + mScroller.getCurrX()
					+ " mScroller.getCurrY() = " + mScroller.getCurrY());
				scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
				postInvalidate();  
//			} else if (mDirection == Direction.RIGHT) {
//				if (mScroller.getCurrX() <= mScreens[mCurScreen]) 
//				{
					// Log.d(TAG, "right mScreens[mCurScreen] = "
					// + mScreens[mCurScreen]);
//					mMoveCount += INTERVAL;
//					if (mMoveCount > mScreens[mCurScreen]) 
//					{
//						mMoveCount = mScreens[mCurScreen];
//						mScroller.abortAnimation();
//					}
//					scrollTo(mMoveCount, mScroller.getCurrY());
					//scrollTo(mScreens[1], mScroller.getCurrY());
//					scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
//					postInvalidate();
//				} 
//				else 
//				{
//					scrollTo(mScreens[mCurScreen], mScroller.getCurrY());
//					mScroller.abortAnimation();
//					  
//				}
//			} else {
//				Log.d(TAG, "computeScroll----forceFinished");
//				scrollTo(mScreens[mCurScreen], mScroller.getCurrY());
//				mScroller.forceFinished(true); // 终止滑动之前要把页面设置回去
//			}
			// Log.d(TAG, "computeScroll----mMoveCount = " + mMoveCount
			// + " getCurrX = " + mScroller.getCurrX());
			//invalidate();
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();
		// Log.d(TAG, "onTouchEvent------------------ ");
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				// mScroller.abortAnimation();
				Log.d(TAG, "-----------onTouchEvent---ACTION_DOWN no finish");
				// return false;
			}
			// mLastMotionX = x;
			// mDownMotionX = x;
			Log.d(TAG, "down mLastMotionX = " + mLastMotionX
					+ " mDownMotionX = " + mDownMotionX);
			break;

		case MotionEvent.ACTION_MOVE:
			final int deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			if (mMotionFinish) {
				mMotionFinish = false;
				mDownMotionX = x;
			}
			Log.d(TAG, "move scroll " + getScrollX() + " mCurScreen = "
					+ mCurScreen + " mTotalPage = " + mTotalPage + " deltaX = "
					+ deltaX + " x = " + x + "mScreens[mCurScreen] = " + mScreens[mCurScreen]); // (getScrollX()) <= mScreens[mTotalPage]
//			if (getScrollX() >= 0 && mCurScreen <= mTotalPage
//					&& (getScrollX() + deltaX < mScreens[mTotalPage - 1])) 
//			{
//				Log.d(TAG, "move scrollX = " + getScrollX() + " deltaX = "
//						+ deltaX + " mMaxWidth = " + mMaxWidth);
//				if (getScrollX() == 0) 
//				{
//					if (deltaX >= 0 || getScrollX() == mScreens[mTotalPage])
//						scrollBy(deltaX, 0);
//				} 
//				else 
//				{
//					scrollBy(deltaX, 0);
//				}
//			}
//			if((mCurScreen == 0 && deltaX < 0) 
//					|| (mCurScreen == mTotalPage - 1 && deltaX > 0))
//			{
//
//			}
//			else
//			{
//				scrollBy(deltaX, 0);
//			}
			//getScrollX() 的值为view所在屏幕最左端区域对应的view的坐标
//			if(mCurScreen == 0)
//			{
//				if(((getScrollX() > 0)|| (getScrollX() == 0 && deltaX > 0))
//						&& (getScrollX() + deltaX <= mScreens[mTotalPage - 1]) //其实只需要这一条和下一条约束就行了
//						&& (getScrollX() + deltaX >= 0)) //当getScrollX = 30 此时快速滑动可能导致deltaX为-40，那么就会出现控件滑过来10的情况
//				{
//					scrollBy(deltaX, 0);
//				}		
//			}
//			else if(mCurScreen == mTotalPage -1)
//			{
//				if(((getScrollX() < mScreens[mCurScreen])
//						|| (getScrollX() == mScreens[mCurScreen] && deltaX < 0))
//						&& (getScrollX() + deltaX >= 0)  //同上
//						&& (getScrollX() + deltaX <= mScreens[mCurScreen])) //这条代码的意义同上
//				{
//					scrollBy(deltaX, 0);
//				}
//			}
//			else
//			{
//				scrollBy(deltaX, 0);
//			}
			if((getScrollX() + deltaX >= 0)
					&& (getScrollX() + deltaX <= mScreens[mTotalPage - 1]))
			{
				scrollBy(deltaX, 0);
			}
				
			break;

		case MotionEvent.ACTION_UP:
			mMotionFinish = true;
			mUpMotionX = x;
			if (mScroller.isFinished()) {
				Log.d(TAG, "onTouchEvent ACTION_UP isFinished");
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				// 获取X轴速率 大于0表示向右滑，小于0表示向左滑
				int velocityX = (int) velocityTracker.getXVelocity();
				Log.d(TAG, "onTouchEvent ACTION_UP velocityX = " + velocityX);
				if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
					// 向左移动
					Log.d(TAG, "left mCurScreen = " + mCurScreen);
					mDirection = Direction.LEFT;
					snapToScreen(mCurScreen - 1);
					
					Intent intent = new Intent("com.apical.radiobuttonchange");  
					mContext.sendBroadcast(intent);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurScreen < mTotalPage - 1) {
					// 向右移动
					Log.d(TAG, "right mCurScreen = " + mCurScreen);
					mDirection = Direction.RIGHT;
					snapToScreen(mCurScreen + 1);
					
					Intent intent = new Intent("com.apical.radiobuttonchange");  
					mContext.sendBroadcast(intent);
				} else {
					Log.d(TAG, "none mCurScreen = " + mCurScreen
							+ " mDownMotionX = " + mDownMotionX + " x = " + x);
					// mDirection = Direction.NONE;
					if ((mUpMotionX - mDownMotionX) < 0
							&& mCurScreen == mTotalPage)
						return true;
					if (mDownMotionX > mUpMotionX) {
						mDirection = Direction.LEFT;
					} else {
						mDirection = Direction.RIGHT;
					}
					snapToDestination(); // 跳到指定页
				}
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			} else {
				Log.d(TAG, "onTouchEvent ACTION_UP not Finished");
			}

			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	/**
	 * 用于拦截手势事件的，每个手势事件都会先调用这个方法。Layout里的onInterceptTouchEvent默认返回值是false,
	 * 这样touch事件会传递到childview控件 ，如果返回false子控件可以响应，否则了控件不响应，这里主要是拦截子控件的响应，
	 * 对ViewGroup不管返回值是什么都会执行onTouchEvent
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// Log.d(TAG, "onInterceptTouchEvent------------------ ");
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			Log.d(TAG, "onInterceptTouchEvent---ACTION_MOVE ");
			// 如果在MOVE过程中返回true不响应子控件，
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		// Log.d(TAG, "onInterceptTouchEvent------------------ state = "
		// + (mTouchState != TOUCH_STATE_REST));
		return mTouchState != TOUCH_STATE_REST;
	}

	/**
	 * 滑动的方向
	 * 
	 * @author Administrator
	 * 
	 */
	public enum Direction {
		LEFT, RIGHT, NONE
	}
	
	public void setSnapSpeed(int snapt)
	{
		snapTime = snapt;
	}
}
