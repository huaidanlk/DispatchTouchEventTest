package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by 坏蛋 on 2017/4/11.
 */

public class MyRecycleView extends RecyclerView {
    private int touchSlop;
    private float xDown, yDown, xMove, yMove;

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    /*
      内部拦截法
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);

                xDown = ev.getRawX();
                yDown = ev.getRawY();
                Log.d("TAG", "onTouch---xDown:" + xDown + "  yDown:" + yDown);

                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getRawX();
                yMove = ev.getRawY();
                int deltaX = (int) (xMove - xDown);
                int deltaY = (int) (yMove - yDown);
                //滑动到起始点右边
                if (Math.abs(deltaX) >= touchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
