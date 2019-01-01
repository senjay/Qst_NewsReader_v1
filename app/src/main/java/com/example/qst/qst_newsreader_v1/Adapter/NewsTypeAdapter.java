package com.example.qst.qst_newsreader_v1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qst.qst_newsreader_v1.Interface.NewsTypeOnItemClickListener;
import com.example.qst.qst_newsreader_v1.R;
//**
// * Author: 钱苏涛.
// * Date: 2018/12/27.
// * Description: 新的顶部适配器 RecyclerView 比原来的好
//**
public class NewsTypeAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    Context context;
    public String[] datas = null;
    private NewsTypeOnItemClickListener onRecyclerViewItemClickListener;
    private int thisPosition;
    public NewsTypeAdapter(Context context, String[] datas) {
        this.context=context;
        this.datas=datas;
    }

    public void setOnRecyclerViewItemClickListener(NewsTypeOnItemClickListener onItemClickListener) {
        this.onRecyclerViewItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsclass_layout, viewGroup, false);
        RecViewViewHolder viewViewHolder = new RecViewViewHolder(view);
        return viewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final int position=i;
        RecViewViewHolder viewViewHolder = (RecViewViewHolder) viewHolder;
        viewViewHolder.mTextView.setText(datas[position]);
        if (viewViewHolder != null) {
            if (position == getthisPosition()) {
                ((RecViewViewHolder) viewHolder).mTextView.setTextColor(Color.parseColor("#FF0000"));
            } else {
                ((RecViewViewHolder) viewHolder).mTextView.setTextColor(Color.parseColor("#000000"));
            }
        }
        if (onRecyclerViewItemClickListener != null) {

            viewViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onRecyclerViewItemClickListener.onClick(position);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.length : 0;
    }
    public int getthisPosition() {
        return thisPosition;
    }
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }


    public static class RecViewViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public RecViewViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.text);

        }
    }

}
