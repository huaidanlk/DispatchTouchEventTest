package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by 坏蛋 on 2017/4/13.
 */

public class MyNestedScrollParentLayout extends LinearLayout implements NestedScrollingParent {
    private static final String TAG = "TAG";
    private View iv_top;
    private int mTopViewHeight;


    public MyNestedScrollParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv_top=getChildAt(0);
        iv_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"imageView is click",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight=iv_top.getMeasuredHeight();
        Log.d(TAG, "onFinishInflate: mTopViewHeight"+mTopViewHeight);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll: "+nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
        if(hiddenTop||showTop){
            Log.e(TAG, "Parent 滑动: "+"  dy="+dy+"  getScrollY="+getScrollY());

            consumed[1]=dy;//完全消费y轴的滑动
            scrollBy(0,dy);
        }

        Log.e(TAG, "onNestedPreScroll: "+"  dy="+dy+"  getScrollY="+getScrollY());
//        super.onNestedPreScroll(target, dx, dy, consumed);
    }
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes)
    {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target)
    {
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        Log.e(TAG, "onNestedScroll");
    }

    @Override
    public void scrollTo(int x, int y) {
        if(y<0){
            y=0;
        }
        if(y>mTopViewHeight){
            y=mTopViewHeight;
        }

        super.scrollTo(x, y);
    }

}
