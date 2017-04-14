package com.example.dispatchtoucheventtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_view_viewGroup).setOnClickListener(this);
        findViewById(R.id.btn_sliding_menu).setOnClickListener(this);
        findViewById(R.id.btn_nested_scrolling).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_view_viewGroup:
                startActivity(new Intent(this,ViewAndViewGroupActivity.class));
                break;
            case R.id.btn_sliding_menu:
                startActivity(new Intent(this,SlidingMenuActivity.class));
                break;
            case R.id.btn_nested_scrolling:
                startActivity(new Intent(this,NestedScrollActivity.class));
                break;
        }
    }
}
