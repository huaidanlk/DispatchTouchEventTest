package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by 坏蛋 on 2017/4/6.
 */

public class SlidingLayout extends RelativeLayout  {
    private static final String TAG = "TAG";
    /**
     * 屏幕宽度值。
     */
    private int screenWidth;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 记录手指按下时的横坐标。(相对屏幕)
     */
    private float xDown;

    /**
     * 记录手指按下时的纵坐标。(相对屏幕)
     */
    private float yDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手指移动时的纵坐标。
     */
    private float yMove;

    private float xLast;
    private float yLase;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;

    /**
     * 右侧布局最多可以滑动到的左边缘。
     */
    private int leftEdge = 0;

    /**
     * 右侧布局最多可以滑动到的右边缘。
     */
    private int rightEdge = 0;

    //左侧菜单布局
    private View leftLayout;

    //
    private MarginLayoutParams leftLayoutParams;

    //内容布局
    private View contentLayout;

    /**
     * 右侧布局的参数，通过此参数来重新确定右侧布局的宽度。
     */
    private MarginLayoutParams contentLayoutParams;


    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop()/2;
        Log.d(TAG, "onTouchEvent: "+touchSlop);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            Log.d(TAG, "onTouchEvent: "+changed);

            //
            leftLayout=getChildAt(0);
            leftLayoutParams= (MarginLayoutParams) leftLayout.getLayoutParams();
            rightEdge=-leftLayoutParams.width;
            //
            contentLayout=getChildAt(1);
            contentLayoutParams= (MarginLayoutParams) contentLayout.getLayoutParams();
//            contentLayoutParams.width=screenWidth;
            contentLayout.setLayoutParams(contentLayoutParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        if (leftLayout.getVisibility() != View.VISIBLE) {
            leftLayout.setVisibility(View.VISIBLE);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown=event.getRawX();
                yDown=event.getRawY();
                xLast=xDown;
                yLase=xDown;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ");
                xMove=event.getRawX();
                yMove=event.getRawY();
                int deltaX = (int) (xMove - xLast);
                int deltaY = (int) (yMove - xLast);
                //向右滑动
                if(deltaX>=touchSlop)
                {
                    Log.d(TAG, "onTouchEvent: "+deltaX);

                    contentLayoutParams.rightMargin=-deltaX;
                    if(contentLayoutParams.rightMargin>leftEdge)
                    {

                    }
                    contentLayout.setLayoutParams(contentLayoutParams);
                }
                //向左滑动
                if(-deltaX>=touchSlop)
                {
                    Log.d(TAG, "onTouchEvent: "+deltaX);

                    contentLayoutParams.rightMargin=-deltaX;
                    if(contentLayoutParams.rightMargin>leftEdge)
                    {

                    }
                    contentLayout.setLayoutParams(contentLayoutParams);
                }
                xLast=xMove;
                yLase=yMove;
                break;
        }


        return true;
    }

    /**
     * 创建VelocityTracker对象，并将触摸事件加入到VelocityTracker当中。
     *
     * @param event
     *            右侧布局监听控件的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

}
