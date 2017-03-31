package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by 坏蛋 on 2017/3/31.
 */

public class MyLayout extends LinearLayout implements View.OnTouchListener{
    private static final String TAG = "TAG";
    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "MyLayout dispatchTouchEvent: "+ev.getAction());

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "MyLayout onInterceptTouchEvent: "+ev.getAction());
//        return true;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, "MyLayout onTouch: "+motionEvent.getAction());
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "MyLayout onTouchEvent: "+event.getAction());
        return super.onTouchEvent(event);
    }
}
