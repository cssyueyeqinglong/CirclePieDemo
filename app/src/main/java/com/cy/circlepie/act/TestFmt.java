package com.cy.circlepie.act;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cy.circlepie.R;
import com.cy.circlepie.view.ExpandLongTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class TestFmt extends Fragment {

    private RecyclerView mListView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<TestActivity.StickyBean> datas;

    public static TestFmt newInstance(ArrayList<TestActivity.StickyBean> datas) {
        Bundle args = new Bundle();
        args.putSerializable("datas", datas);
        TestFmt fragment = new TestFmt();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_recycleview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExpandLongTextView tv = (ExpandLongTextView) view.findViewById(R.id.tv_long);
        tv.initWidth(getWindowWidth());
        tv.setMaxLines(4);
        tv.setExpandText("京东撒个撒个到公司大的的撒个撒个撒个到公司大的的的的广东省打个京东撒个公司大的的的的广东省打个京东撒个撒个到公个到公司大的的的的的广东省的广东省打个京东撒个撒个到公司大的的gas的gas的gas广东省打个京东撒个撒个sadgasgas的gas的gas的gas的gas广东省打个京东撒个撒个sadgas到公司大gas的gas的gas的gas的gas广东省打个京东撒个撒个sadgas到公司大gas的gas的gas的gas的gas广东省打个京东撒个撒个sadgas到公司号大gas的gas的gas的gas的gas广东省打个省打个");
    }


    public int getWindowWidth() {
        DisplayMetrics ma = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(ma);
        return ma.widthPixels;

    }


}
