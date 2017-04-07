package com.example.dispatchtoucheventtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by 坏蛋 on 2017/3/31.
 */

public class ViewAndViewGroupActivity extends AppCompatActivity {
    private LinearLayout ll_layout;
    private Button btn_button;
    private ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_viewgroup);

        initView();

        /*事件首先是从Activity 分发到 decorView 然后在到 rootView ( 即setContentView()所设置的view)

        * 当viewGroup开始分发事件的时候，会先调用 viewGroup.dispatchTouchEvent()
        * 在 该方法内，会先判断 viewGroup 是否会拦截改事件，拦截函数是onInterceptTouchEvent(),
        * 该函数默认返回false ,即不拦截。
        * 如果不拦截，就会遍历其子view 并调用子 view 的 dispatchTouchEvent() 方法，进行事件的传递，
        * 如果 子View 不处理改事件 即 view.dispatchTouchEvent() 返回为false ,那么 回朔到其父viewGroup
        * 来处理该事件，此时调用的是viewGroup的 super.dispatchTouchEvent()。
        * 如果 view都不对该事件做处理，则会传递个activity 来处理，会调用 activity.onTouchEvent() 来处理
        * */

        /*测试 MyLayout onInterceptTouchEvent return false ,不拦截事件

                当点击空白区域的时，MyLayout viewGroup.dispatchTouchEvent
                                    ->MyLayout onInterceptTouchEvent
                                      ->MyLayout super.dispatchTouchEvent
                                        ->MyLayout onTouch
                                          ->MyLayout onTouchEvent
        *
        *      当点击button 时  MyLayout dispatchTouchEvent
                                    ->MyLayout onInterceptTouchEvent
                                       ->MyButton onTouchEvent
                                           ->MyButton dispatchTouchEvent
                                               ->MyButton onTouchEvent
                在view.dispatchTouchEvent()中，会先调用onTouch(),如果button 注册了
                OnTouchListener 事件并在onTouch()中返回了true,不会进入 onTouchEvent()则 onClick 事件会被拦截
                如果 返回了 false 会进入 onTouchEvent() 则 onClick 不会被拦截
                此时 view.dispatchTouchEvent() 仍然会返回 true 即事件被消耗了
                这是因为 尽管在onTouch()中返回了false  因为button的 是可以点击的，
                所以 onTouchEvent()一定会返回true ,即button 消耗了事件

                public boolean dispatchTouchEvent(MotionEvent event) {
                    if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&
                         mOnTouchListener.onTouch(this, event)) {
                            return true;
                    }
                    return onTouchEvent(event);
}

        * */

        /*在看郭老师的事件分发博客时 http://blog.csdn.net/guolin_blog/article/details/9097463/
        *里面一句话 dispatchTouchEvent在进行事件分发的时候，只有前一个action返回true，才会触发后一个action
        *一直不明白为什么，以为是MotionEvent事件的内部定则，后来验证的时候发现，如果在onTouchEvent的ACTION_DOWN
        * 里返回了true,但是在ACTION_MOVE里面返回了false，同样会触发后面一系列的ACTION_MOVE ACTION_UP事件，这个
        * 这个现象引起了我的注意，觉得郭老师这句话说的不对，经过后面的分析源码，找到了答案
        * 当ViewGroup dispatchTouchEvent 时
        * 如果是DOWN事件并且返回true，View所在的容器会记录当前DOWN事件被那个View消费了，会有个target对象被赋值为
        * 该View 下次容器派发非DOWN事件时，就不会去遍历子View了，而是直接调用target的dispatchTouchEvent，
        * 同时，后续的MotionEvent事件将会全部有该target来处理，此时，不管在ACTION_MOVE或者ACTION_UP返回false
        * 都不会影响事件流被改View 截获
        * 反之，如果当前view返回的是false，容器就会将事件派发给其他的View，直到有子View的 dispatchTouchEvent 返回true
        * 如果没有子View 返回true 将会回朔调用其 super.dispatchTouchEvent
        *
        *
        *
        * */

        ll_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TAG", "MyLayout onTouch: " + event.getAction());

                return false;
                //                return true;
            }
        });
        ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "MyLayout onClick: ");
                Toast.makeText(view.getContext(), "myLayout is click!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("TAG", "MyButton onTouch: " + motionEvent.getAction());
                //                return true;//返回true后 不会执行 onTouchEvent() onClick 事件无法触发
                return false;  //返回false后 会执行 onTouchEvent() onClick 事件可以触发
            }
        });
        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "MyButton onClick: ");
                Toast.makeText(view.getContext(), "myButton is click!", Toast.LENGTH_SHORT).show();
            }
        });

        iv_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("TAG", "MyImage onTouch: " + motionEvent.getAction());
                //前提是没有给imageViwe 设置click事件,如果设置了，同 button 一样
                //                return true;//返回true  后不会执行 onTouchEvent()   onClick 事件不会触发
                return false;//返回false 会执行 onTouchEvent()  但onClick 事件不会触发
            }
        });
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "MyImage onClick: ");
                Toast.makeText(view.getContext(), "myImage is click!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        btn_button = (Button) findViewById(R.id.btn_button);
        iv_image = (ImageView) findViewById(R.id.iv_image);
    }

}
