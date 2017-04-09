package com.example.dispatchtoucheventtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dispatchtoucheventtest.R;

/**
 * Created by 坏蛋 on 2017/4/9.
 */

public class TestAdapter1 extends BaseAdapter {
    private String[] list;

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        else
            return list.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_content,parent,false);
         TextView textView= (TextView) view.findViewById(R.id.tv_test);
        textView.setText(list[position]);
        return view;
    }
    public void setData(String[] list){
        this.list=list;
    }
}
