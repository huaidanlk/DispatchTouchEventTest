package com.example.dispatchtoucheventtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.dispatchtoucheventtest.adapter.TestAdapter;
import com.example.dispatchtoucheventtest.adapter.TestAdapter1;
import com.example.dispatchtoucheventtest.view.SlidingLayout;

/**
 * Created by 坏蛋 on 2017/4/6.
 */

public class SlidingMenuActivity extends AppCompatActivity {
    private RecyclerView rv_test;
    private ListView lv_test;
    private TestAdapter testAdapter;
    private TestAdapter1 testAdapter1;
    private SlidingLayout slidingLayout;
    private String testContent[] = {
            "testContent1", "testContent2", "testContent3",
            "testContent1", "testContent2", "testContent3",
            "testContent4", "testContent5", "testContent6",
            "testContent4", "testContent5", "testContent6",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);
        initView();
        initData();
    }

    private void initView() {
        slidingLayout= (SlidingLayout) findViewById(R.id.sliding_layout);

        rv_test = (RecyclerView) findViewById(R.id.rv_test);
        testAdapter = new TestAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_test.setLayoutManager(linearLayoutManager);
        rv_test.setAdapter(testAdapter);
        slidingLayout.setScrollEvent(rv_test);


  /*      lv_test= (ListView) findViewById(R.id.lv_test);
        testAdapter1=new TestAdapter1();
        lv_test.setAdapter(testAdapter1);
        slidingLayout.setScrollEvent(lv_test);
        lv_test.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = testContent[position];
                Toast.makeText(SlidingMenuActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initData() {
        testAdapter.setData(testContent);
//        testAdapter1.setData(testContent);
    }
}
