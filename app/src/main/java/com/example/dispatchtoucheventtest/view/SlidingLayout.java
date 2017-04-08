package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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

public class SlidingLayout extends RelativeLayout {
    private static final String TAG = "TAG";
    private static final int SCROLL_TO_RIGHT = 1;
    private static final int SCROLL_TO_LEFT = 2;
    private static final int SCROLL_DELAY_time = 10;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;
    //
    private int contentRightMarginMin;

    private int contentShow = 100;
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
    private float yLast;

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

    /**
     * 滚动显示和隐藏左侧布局时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 400;
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL_TO_RIGHT:
                    Log.d(TAG, "handler: " + contentLayoutParams.rightMargin);

                    if (contentLayoutParams.rightMargin < -(screenWidth - contentShow)) {
                        contentLayoutParams.rightMargin = -(screenWidth - contentShow);
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    }
                    contentLayoutParams.rightMargin = contentLayoutParams.rightMargin - SCROLL_DELAY_time;
                    contentLayout.setLayoutParams(contentLayoutParams);

                    Message msg1 = obtainMessage();
                    msg1.what = SCROLL_TO_RIGHT;
                    msg1.arg1 = msg.arg1 - SCROLL_DELAY_time;
                    sendMessageDelayed(msg1, 10);
                    break;
                case SCROLL_TO_LEFT:
                    Log.d(TAG, "handler: " + contentLayoutParams.rightMargin);

                    if (contentLayoutParams.rightMargin >0) {
                        contentLayoutParams.rightMargin = 0;
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    }
                    contentLayoutParams.rightMargin = contentLayoutParams.rightMargin + SCROLL_DELAY_time;
                    contentLayout.setLayoutParams(contentLayoutParams);

                    Message msg2 = obtainMessage();
                    msg2.what = SCROLL_TO_LEFT;
                    msg2.arg1 = msg.arg1 - SCROLL_DELAY_time;
                    sendMessageDelayed(msg2, 10);
                    break;
            }
        }
    };

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        contentRightMarginMin = (int) (getResources().getDisplayMetrics().density * 50) - screenWidth;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 16;
        //        Log.d(TAG, "onTouchEvent: "+touchSlop);
        //        Log.d(TAG, "screenWidth: " + screenWidth);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getMeasuredWidth();
        //        Log.d(TAG, "screenWidth_onMeasure: " + screenWidth);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //            Log.d(TAG, "onTouchEvent: "+changed);

            //
            leftLayout = getChildAt(0);
            leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
            //            rightEdge=-leftLayoutParams.width;
            //
            contentLayout = getChildAt(1);
            contentLayoutParams = (MarginLayoutParams) contentLayout.getLayoutParams();
            contentLayoutParams.width = screenWidth;
            //            Log.d(TAG, "onTouchEvent: "+contentLayoutParams.width);

            contentLayout.setLayoutParams(contentLayoutParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
      /*  if (leftLayout.getVisibility() != View.VISIBLE) {
            leftLayout.setVisibility(View.VISIBLE);
        }*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //                Log.d(TAG, "onTouchEvent: ");
                xMove = event.getRawX();
                yMove = event.getRawY();
                int deltaX = (int) (xMove - xDown);
                int deltaY = (int) (yMove - yDown);
                //滑动到起始点右边
                if (deltaX >= touchSlop) {
                                        Log.d(TAG, "onTouchEvent: "+deltaX);

                    contentLayoutParams.rightMargin = -deltaX;
                    if (contentLayoutParams.rightMargin < contentRightMarginMin) {
                        contentLayoutParams.rightMargin = contentRightMarginMin;
                    }
                    contentLayout.setLayoutParams(contentLayoutParams);
                    //                    Log.d(TAG, "move_left: "+deltaX);
                }
                //滑动到起始点左边
                if (-deltaX >= touchSlop) {
                    //                    Log.d(TAG, "move_left: " + deltaX + " right_margin" + contentLayoutParams.rightMargin);
                    contentLayoutParams.rightMargin = rightEdge - deltaX;
                    if (contentLayoutParams.rightMargin < rightEdge || xMove <= xDown) {
                        contentLayoutParams.rightMargin = rightEdge;
                    }
                    contentLayout.setLayoutParams(contentLayoutParams);
                    //                    Log.d(TAG, "move_left: "+deltaX+" right_margin"+contentLayoutParams.rightMargin);

                }

                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                int upDeltaX = (int) (xUp - xDown);
                //起始点右边
                if (upDeltaX > 0) {
                    if (upDeltaX > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY) {
                        scrollToLeftLayout((int) (screenWidth - xUp));
                    }else {
                        scrollToRightLayout((upDeltaX));

                    }
                }
                //起始点左边
                if (-upDeltaX > 0) {
                    if (-upDeltaX > screenWidth / 2 || -getScrollVelocity() > SNAP_VELOCITY) {
                        scrollToLeftLayout((int) (screenWidth - xUp));
                    }
                }
                break;
        }


        return true;
    }

    /**
     * 创建VelocityTracker对象，并将触摸事件加入到VelocityTracker当中。
     *
     * @param event 右侧布局监听控件的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在右侧布局的监听View上的滑动速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 将屏幕滚动到左侧布局界面，滚动速度设定为10.
     */
    public void scrollToLeftLayout(int scrollDistance) {
        Message message = Message.obtain();
        message.what = SCROLL_TO_RIGHT;
        message.arg1 = scrollDistance;
        handler.sendMessageDelayed(message, SCROLL_DELAY_time);
    }

    /**
     * 将屏幕滚动到右侧布局界面，滚动速度设定为10.
     */
    public void scrollToRightLayout(int scrollDistance) {
        Message message = Message.obtain();
        message.what = SCROLL_TO_LEFT;
        message.arg1 = scrollDistance;
        handler.sendMessageDelayed(message, SCROLL_DELAY_time);
    }
}
