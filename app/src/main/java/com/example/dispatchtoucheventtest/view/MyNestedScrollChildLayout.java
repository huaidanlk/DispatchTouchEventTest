package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by 坏蛋 on 2017/4/13.
 */

public class MyNestedScrollChildLayout extends LinearLayout implements NestedScrollingChild {
    private static final String TAG = "TAG";
    private NestedScrollingChildHelper mScrollingChildHelper;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mLastTouchX;
    private int mLastTouchY;
    private int showHeight;

    public MyNestedScrollChildLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /*是否可以嵌套滑动
    * */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.d(TAG, "setNestedScrollingEnabled:" + enabled);
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }
    @Override
    public boolean isNestedScrollingEnabled() {
        Log.d(TAG, "isNestedScrollingEnabled");
        return getScrollingChildHelper().isNestedScrollingEnabled();

    }
    @Override
    public boolean startNestedScroll(int axes) {
        Log.d(TAG, "startNestedScroll:" + axes);
        return getScrollingChildHelper().startNestedScroll(axes);
    }
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        Log.d(TAG, "dispatchNestedPreScroll:dx" + dx + ",dy:" + dy + ",consumed:" + consumed + ",offsetInWindow:" + offsetInWindow);
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }


    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
            mScrollingChildHelper.setNestedScrollingEnabled(true);
        }
        return mScrollingChildHelper;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xMove = (int)(event.getX() + 0.5f);
        int yMove = (int) (event.getRawY() + 0.5f);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
                startNestedScroll(nestedScrollAxis);

                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastTouchX - xMove;
                int dy = mLastTouchY - yMove;

                //如果父View 需要处理滑动事件
                if (dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset)) {
//                    Log.d(TAG, "parent_move:dy:" + dy + ",mLastTouchY:" + mLastTouchY + ",yMove;" + yMove);
//
//                    dy -= mScrollConsumed[1];
//                    if (dy == 0) {
//                        return true;
//                    }
                } else {
                    scrollBy(0, dy);
                }
                break;
        }
        mLastTouchX = xMove;
        mLastTouchY = yMove;
        return true;
    }
    @Override
    public void scrollTo(int x, int y) {
        int MaxY = getMeasuredHeight() - showHeight;
        if (y > MaxY) {
            y = MaxY;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (showHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            showHeight = getMeasuredHeight();
        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000000, MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
