package com.cy.circlepie.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cy.circlepie.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/16.
 */

public class TestActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private TextView mTvOther;
    public ArrayList<StickyBean> replyList_other = new ArrayList<>();//其他回答

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
        mViewPager = (ViewPager) findViewById(R.id.xrecyce_view);
        mIndicator = (TabPageIndicator) findViewById(R.id.tab);
        replyList_other.add(new StickyBean("风花雪月", 0));
        replyList_other.add(new StickyBean("风花雪月", 0));
        replyList_other.add(new StickyBean("十里飘香", 0));
        replyList_other.add(new StickyBean("龙飞凤舞", 0));
        replyList_other.add(new StickyBean("春花秋月", 0));
        replyList_other.add(new StickyBean("花前月下", 0));
        replyList_other.add(new StickyBean("风驰电掣", 0));
        replyList_other.add(new StickyBean("雾里看花", 0));
        replyList_other.add(new StickyBean("吹角连营", 0));
        replyList_other.add(new StickyBean("张灯结彩", 0));
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mIndicator.setViewPager(mViewPager);
    }

    public class StickyBean {
        String content;
        int type;

        public StickyBean(String content, int type) {
            this.content = content;
            this.type = type;
        }
    }

    private class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TestFmt.newInstance(replyList_other);
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.tab_selector;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "作死了呵呵";
        }
    }
}
