package com.cy.circlepie;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private List<String> mDatas;

    private Context mContext;
    private LayoutInflater mInflater;

    public MainAdapter(Context context, List<String> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(mContext);
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,UIUtils.dip2dx(mContext,40));
        textView.setLayoutParams(params);
        return new MainHolder(textView);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, final int position) {
        final String item = mDatas.get(position);
        holder.tv.setText(item);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickLisenter != null) {
                    mItemClickLisenter.onItemClick(item, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class MainHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MainHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }

    public interface OnItemClickLisenter {
        void onItemClick(String item, int position);
    }

    private OnItemClickLisenter mItemClickLisenter;

    public void setOnItemClickLisenter(OnItemClickLisenter lisenter) {
        this.mItemClickLisenter = lisenter;
    }
}
