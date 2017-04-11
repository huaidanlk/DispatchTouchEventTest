package com.example.dispatchtoucheventtest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dispatchtoucheventtest.R;

/**
 * Created by 坏蛋 on 2017/4/9.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private String[] list;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_content,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_test.setText(list[position]);
        holder.tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"recycleView "+list[position],Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list==null)
            return 0;
        return list.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_test;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_test= (TextView) itemView.findViewById(R.id.tv_test);
        }
    }
    public void setData(String[] list){
        this.list = list;
        }
}
