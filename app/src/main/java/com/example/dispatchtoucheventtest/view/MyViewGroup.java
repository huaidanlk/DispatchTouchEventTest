package com.example.dispatchtoucheventtest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 坏蛋 on 2017/4/19.
 */

public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* widthMeasureSpec,heightMeasureSpec是父类传递过来给当前view的一个建议值,
     * 即想把当前view的尺寸设置为宽widthMeasureSpec,高heightMeasureSpec
     * 该值是由 getChildMeasureSpec()得到的，是由 parent的 MeasureSpec 和child的LayoutParams 构造出的
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility()!=View.GONE){
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams  lp=(MarginLayoutParams)child.getLayoutParams();
                int childWidth = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
                int childHeight = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
                height += childHeight;
                width = Math.max(childWidth, width);
            }
        }
        /*用来支持 ViewGroup的padding属性
        * */
        height += getPaddingBottom()+getPaddingTop();
        width+=getPaddingLeft()+getPaddingRight();

        /*当 ViewGroup 的 layout_height=wrap_content 的时候 其高度应该是所有子View的高度总和
        * */
        setMeasuredDimension(measureWidthMode == MeasureSpec.EXACTLY ? measureWidth : width,
                measureHeight == MeasureSpec.EXACTLY ? measureHeight : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top=0;
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child=getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            child.layout(0,top,childWidth,top+childHeight);

            top+=childHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }
}
