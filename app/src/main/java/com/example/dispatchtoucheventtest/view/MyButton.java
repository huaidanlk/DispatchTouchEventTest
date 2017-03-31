package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by 坏蛋 on 2017/3/31.
 */

public class MyButton extends Button implements View.OnTouchListener{
    private static final String TAG = "TAG";

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "MyButton dispatchTouchEvent: "+event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "MyButton onTouchEvent: "+event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, "MyButton onTouch: "+motionEvent.getAction());
        return false;
    }
}
