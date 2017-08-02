package com.cy.circlepie.act;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.cy.circlepie.R;
import com.cy.circlepie.view.CirclePieView;
import com.cy.circlepie.view.FlowLayouView;
import com.cy.circlepie.view.PictureGameAct;
import com.cy.circlepie.view.View2048;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/28.
 */

public class CirclePieAct extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();

    @OnClick(R.id.iv_back)
    public void back(View view) {
        finish();
    }

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.fl_view)
    FrameLayout mFlView;

    public static final String CirclePie_TYPE = "CirclePie_TYPE";
    public static final String CirclePie_TITLE = "CirclePie_TITLE";
    private int contentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_view);
        ButterKnife.bind(this);
        contentType = getIntent().getIntExtra(CirclePie_TYPE, 0);
        String title = getIntent().getStringExtra(CirclePie_TITLE);
        mTvTitle.setText(title);
        View contentView;
        switch (contentType) {
            case 0://饼状图
                contentView = new CirclePieView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                contentView.setLayoutParams(params);
                break;
            case 1://流式布局——标签墙
                contentView = new FlowLayouView(this);
//                contentView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                ((FlowLayouView) contentView).setHorizontalSpace(20);
                ((FlowLayouView) contentView).setVerticalSpace(30);
                FrameLayout.LayoutParams params01 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                contentView.setLayoutParams(params01);
//                ((FlowLayouView) contentView).setSameSpan(true);//设置多余空间是否均分
                ((FlowLayouView) contentView).initDatas();
                break;
            case 2://2048小游戏
                contentView = new View2048(this);
                ViewGroup.LayoutParams params02 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                contentView.setLayoutParams(params02);
                break;
            default:
                contentView = new TextView(this);
                ((TextView) contentView).setText("你好哟，欢迎光临");
                break;
        }
        mFlView.addView(contentView);

    }
}
