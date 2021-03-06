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

public class SlidingLayout extends RelativeLayout implements View.OnTouchListener {
    private static final String TAG = "TAG";
    private static final int SCROLL_TO_RIGHT_LAYOUT = 1;
    private static final int SCROLL_TO_LEFT_LAYOUT = 2;
    private static final int SCROLL_DELAY_time = 10;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;
    //
    private int contentRightMarginMin;
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
    /**
     * 用于监听侧滑事件的View。
     */
    private View mBindView;
    //
    private MarginLayoutParams leftLayoutParams;

    //内容布局
    private View contentLayout;

    private boolean isLeftLayoutDisplay = false;
    private boolean isSliding = false;
    /**
     * 右侧布局的参数，通过此参数来重新确定右侧布局的宽度。
     */
    private MarginLayoutParams contentLayoutParams;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL_TO_LEFT_LAYOUT:
                    Log.d(TAG, "handler:TO_LEFT_LAYOUT " + contentLayoutParams.rightMargin);
                    //停止滑动,左侧布局完全显示，内容布局隐藏
                    if (contentLayoutParams.rightMargin < contentRightMarginMin) {
                        contentLayoutParams.rightMargin = contentRightMarginMin;
                        contentLayout.setLayoutParams(contentLayoutParams);
                        isLeftLayoutDisplay = true;
                        isSliding = false;
                        break;
                    }
                    isSliding = true;
                    contentLayoutParams.rightMargin = contentLayoutParams.rightMargin - SCROLL_DELAY_time;
                    contentLayout.setLayoutParams(contentLayoutParams);

                    Message msg1 = obtainMessage();
                    msg1.what = SCROLL_TO_LEFT_LAYOUT;
                    msg1.arg1 = msg.arg1 - SCROLL_DELAY_time;
                    sendMessageDelayed(msg1, 10);
                    break;
                case SCROLL_TO_RIGHT_LAYOUT:
                    Log.d(TAG, "handler:TO_RIGHT_LAYOUT " + contentLayoutParams.rightMargin);

                    if (contentLayoutParams.rightMargin > 0) {
                        contentLayoutParams.rightMargin = 0;
                        contentLayout.setLayoutParams(contentLayoutParams);
                        isLeftLayoutDisplay = false;
                        isSliding = false;
                        break;
                    }
                    isSliding = true;
                    contentLayoutParams.rightMargin = contentLayoutParams.rightMargin + SCROLL_DELAY_time;
                    contentLayout.setLayoutParams(contentLayoutParams);

                    Message msg2 = obtainMessage();
                    msg2.what = SCROLL_TO_RIGHT_LAYOUT;
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
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.d(TAG, "touchSlop: " + touchSlop);
        //        Log.d(TAG, "screenWidth: " + screenWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getMeasuredWidth();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
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

    /*
    * 外部拦截法 源自刚哥的书 ACTION_DOWN事件不能拦截，不然子View 将完全收不到任何事件，全部被父ViewGroup处理了
    * 在ACTION_MOVE处理需要拦截的逻辑，交给父ViewGroup处理，拦截后会调用
    * */
 /*   @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getRawX();
                yMove = ev.getRawY();
                int deltaX = (int) (xMove - xDown);
                int deltaY = (int) (yMove - yDown);
                if (Math.abs(deltaX) >= touchSlop && Math.abs(deltaY) < touchSlop) {
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //会屏蔽子控件 onClick 事件
                //                intercepted=true;
                intercepted = false;
                break;
        }

        return intercepted;
    }*/

    /*
        * 内部拦截法
        * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            xDown = ev.getRawX();
            yDown = ev.getRawY();
            Log.d(TAG, "onInterceptTouchEvent---xDown:" + xDown + "  yDown:" + yDown);

            return false;
        } else
            return true;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        Log.d(TAG, "onTouch---xDown:" + xDown + "  yDown:" + yDown);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);

                xDown = event.getRawX();
                yDown = event.getRawY();
                Log.d(TAG, "onTouch---xDown:" + xDown + "  yDown:" + yDown);

                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                int deltaX = (int) (xMove - xDown);
                int deltaY = (int) (yMove - yDown);
                //滑动到起始点右边
                if (deltaX >= touchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    Log.d(TAG, "onTouch: " + deltaX + "  xDown  " + xDown);
                    //如果左侧布局未完全显示，内容布局未隐藏
                    if (!isLeftLayoutDisplay) {
                        isSliding = true;
                        contentLayoutParams.rightMargin = -deltaX;
                        if (contentLayoutParams.rightMargin < contentRightMarginMin) {
                            contentLayoutParams.rightMargin = contentRightMarginMin;
                        }
                        contentLayout.setLayoutParams(contentLayoutParams);
                    }

                }
                //滑动到起始点左边
                if (-deltaX >= touchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(false);

                    //                    Log.d(TAG, "move_left: " + deltaX + " right_margin" + contentLayoutParams.rightMargin);
                    //如果左侧布局完全显示，内容布局隐藏
                    if (isLeftLayoutDisplay) {
                        isSliding = true;
                        contentLayoutParams.rightMargin = contentRightMarginMin - deltaX;
                      /*  if (contentLayoutParams.rightMargin < rightEdge || xMove <= xDown) {
                            contentLayoutParams.rightMargin = rightEdge;
                        }*/
                        contentLayout.setLayoutParams(contentLayoutParams);
                        Log.d(TAG, "move_left: " + deltaX + " right_margin" + contentLayoutParams.rightMargin);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                int upDeltaX = (int) (xUp - xDown);
                Log.d(TAG, "upDeltaX: " + upDeltaX);

                //起始点右边
                if (upDeltaX > touchSlop && !isLeftLayoutDisplay) {
                    if (upDeltaX > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY) {
                        scrollToLeftLayout((int) (screenWidth - xUp));
                    } else {
                        scrollToRightLayout((upDeltaX));
                    }
                }
                //起始点左边 左侧布局已经完全显示
                if (-upDeltaX > touchSlop && isLeftLayoutDisplay) {
                    if ((upDeltaX - contentRightMarginMin) < screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY) {
                        scrollToRightLayout((upDeltaX));
                    } else {
                        scrollToLeftLayout((int) (xUp));
                    }
                }
                recycleVelocityTracker();
                break;
        }
/*        if (v.isEnabled()) {
            if (isSliding) {
                unFocusBindView();
                return true;
            }
            if (isLeftLayoutDisplay) {
                return true;
            }
            return false;
        }*/
        return false;
    }

    /**
     * 使用可以获得焦点的控件在滑动的时候失去焦点。
     */
    private void unFocusBindView() {
        if (mBindView != null) {
            mBindView.setPressed(false);
            mBindView.setFocusable(false);
            mBindView.setFocusableInTouchMode(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                Log.d(TAG, "xDown:yDown ");

                break;
            case MotionEvent.ACTION_MOVE:
                //                Log.d(TAG, "onTouchEvent: ");
                xMove = event.getRawX();
                yMove = event.getRawY();
                int deltaX = (int) (xMove - xDown);
                int deltaY = (int) (yMove - yDown);
                //滑动到起始点右边
                if (deltaX >= touchSlop) {
                    Log.d(TAG, "onTouchEvent: " + deltaX);
                    //如果左侧布局未完全显示，内容布局未隐藏
                    if (!isLeftLayoutDisplay) {
                        contentLayoutParams.rightMargin = -deltaX;
                        if (contentLayoutParams.rightMargin < contentRightMarginMin) {
                            contentLayoutParams.rightMargin = contentRightMarginMin;
                        }
                        contentLayout.setLayoutParams(contentLayoutParams);
                    }

                }
                //滑动到起始点左边
                if (-deltaX >= touchSlop) {
                    //                    Log.d(TAG, "move_left: " + deltaX + " right_margin" + contentLayoutParams.rightMargin);
                    //如果左侧布局完全显示，内容布局隐藏
                    if (isLeftLayoutDisplay) {
                        contentLayoutParams.rightMargin = contentRightMarginMin - deltaX;
                      /*  if (contentLayoutParams.rightMargin < rightEdge || xMove <= xDown) {
                            contentLayoutParams.rightMargin = rightEdge;
                        }*/
                        contentLayout.setLayoutParams(contentLayoutParams);
                        Log.d(TAG, "move_left: " + deltaX + " right_margin" + contentLayoutParams.rightMargin);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                int upDeltaX = (int) (xUp - xDown);
                Log.d(TAG, "upDeltaX: " + upDeltaX);

                //起始点右边
                if (upDeltaX > touchSlop && !isLeftLayoutDisplay) {
                    if (upDeltaX > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY) {
                        scrollToLeftLayout((int) (screenWidth - xUp));
                    } else {
                        scrollToRightLayout((upDeltaX));
                    }
                }
                //起始点左边 左侧布局已经完全显示
                if (-upDeltaX > touchSlop && isLeftLayoutDisplay) {
                    if ((upDeltaX - contentRightMarginMin) < screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY) {
                        scrollToRightLayout((upDeltaX));
                    } else {
                        scrollToLeftLayout((int) (xUp));
                    }
                }
                break;
        }
        if (isLeftLayoutDisplay)
            return true;
        return false;
    }

    /**
     * 绑定监听侧滑事件的View，即在绑定的View进行滑动才可以显示和隐藏左侧布局。
     *
     * @param bindView 需要绑定的View对象。
     */
    public void setScrollEvent(View bindView) {
        mBindView = bindView;
//        mBindView.setOnTouchListener(this);
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
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 将屏幕滚动到左侧布局界面，滚动速度设定为10.
     */
    public void scrollToLeftLayout(int scrollDistance) {
        Message message = Message.obtain();
        message.what = SCROLL_TO_LEFT_LAYOUT;
        message.arg1 = Math.abs(scrollDistance);
        handler.sendMessageDelayed(message, SCROLL_DELAY_time);
    }

    /**
     * 将屏幕滚动到右侧布局界面，滚动速度设定为10.
     */
    public void scrollToRightLayout(int scrollDistance) {
        Message message = Message.obtain();
        message.what = SCROLL_TO_RIGHT_LAYOUT;
        message.arg1 = Math.abs(scrollDistance);
        handler.sendMessageDelayed(message, SCROLL_DELAY_time);
    }


}
