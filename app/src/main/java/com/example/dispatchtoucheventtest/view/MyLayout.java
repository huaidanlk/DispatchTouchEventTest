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
        /*返回true时，表示 viewGroup要拦截改事件，事件将不会传递到其子view，
        * 而是会传递到自己的onTouchEvent()中
        * */
//        return true;

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout onInterceptTouchEvent:ACTION_DOWN "+ev.getAction());
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout onInterceptTouchEvent:ACTION_MOVE "+ev.getAction());
                //在ACTION_MOVE 时 拦截子View 的在ACTION_MOVE事件，那么子View 将不会收到
                //后面一系列的事件流(UP/CANCEL 等等)
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout onInterceptTouchEvent:ACTION_UP "+ev.getAction());
                return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, "MyLayout onTouch: "+motionEvent.getAction());
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "MyLayout onTouchEvent:begin "+event.getAction());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout onTouchEvent:ACTION_DOWN "+event.getAction());
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout onTouchEvent:ACTION_MOVE "+event.getAction());
                return false;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout onTouchEvent:ACTION_UP "+event.getAction());
                return true;
        }
        Log.d(TAG, "MyLayout onTouchEvent:super "+event.getAction());

        return super.onTouchEvent(event);
    }
}
