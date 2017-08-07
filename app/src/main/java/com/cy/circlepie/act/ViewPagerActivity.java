package com.cy.circlepie.act;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.circlepie.R;
import com.cy.circlepie.StickyItemDection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerActivity extends AppCompatActivity {
    private float mMinScale = 0.85f;//viewpager图片的缩放比例
    public static final float DEFAULT_CENTER = 0.5f;
    public static final int TYPE_STICKY = 1;
    public static final int TYPE_NORMAL = 0;
    private RecyclerView mRecyclerView;
    private String[] data;
    private RecyclerView mRcvSticky;
    private List<StickyBean> stickyDatas;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        viewPager.setPageMargin(20);
        viewPager.setOffscreenPageLimit(3);
        data = new String[]{"a", "b", "c"};
        viewPager.setAdapter(new BitmapAdapter(new ArrayList<String>(Arrays.asList(data)), this));
        viewPager.setPageTransformer(true, new ScaleInTransformer());


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new ViewAdapter());

        mRcvSticky = (RecyclerView) findViewById(R.id.recycler_sticky);
        mRcvSticky.setLayoutManager(new LinearLayoutManager(ViewPagerActivity.this, LinearLayoutManager.HORIZONTAL, false));
        stickyDatas = new ArrayList<>();
        stickyDatas.add(new StickyBean("风花雪月", 0));
        stickyDatas.add(new StickyBean("十里飘香", 0));
        stickyDatas.add(new StickyBean("龙飞凤舞", 0));
        stickyDatas.add(new StickyBean("春花秋月", 0));
        stickyDatas.add(new StickyBean("花前月下", 0));
        stickyDatas.add(new StickyBean("风驰电掣", 0));
        stickyDatas.add(new StickyBean("雾里看花", 0));
        stickyDatas.add(new StickyBean("吹角连营", 0));
        stickyDatas.add(new StickyBean("张灯结彩", 0));
        mRcvSticky.setAdapter(new StickyAdapter(stickyDatas));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItem = mLayoutManager.findFirstVisibleItemPosition();
                mRcvSticky.setVisibility(firstItem >= 6 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private class ViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder mHolder;
            if (viewType == TYPE_STICKY) {//吸附效果的item
                RecyclerView rcv = new RecyclerView(ViewPagerActivity.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2dx(50));
                rcv.setLayoutParams(params);
                rcv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mHolder = new StickyViewHolder(rcv);
            } else {
                TextView tv = new TextView(ViewPagerActivity.this);
                tv.setGravity(Gravity.CENTER);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2dx(50));
                tv.setLayoutParams(params);
                mHolder = new TestViewHolder(tv);
            }
            return mHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_STICKY) {//吸附
                RecyclerView rcv = (RecyclerView) holder.itemView;
                rcv.setLayoutManager(new LinearLayoutManager(ViewPagerActivity.this, LinearLayoutManager.HORIZONTAL, false));
                rcv.setAdapter(new StickyAdapter(stickyDatas));

            } else {
                ((TextView) holder.itemView).setText("位置：" + position);
            }
        }

        @Override
        public int getItemCount() {
            return data.length * 10;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 6 ? TYPE_STICKY : TYPE_NORMAL;
        }
    }

    private class TestViewHolder extends RecyclerView.ViewHolder {

        public TestViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class StickyViewHolder extends RecyclerView.ViewHolder {

        public StickyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class StickyAdapter extends RecyclerView.Adapter<TestViewHolder> {
        List<StickyBean> datas;

        StickyAdapter(List<StickyBean> datas) {
            this.datas = datas;
        }

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(ViewPagerActivity.this);
            tv.setGravity(Gravity.CENTER);
            tv.setLines(1);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dp2dx(80), ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(params);

            return new TestViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView;
            tv.setText(datas.get(position).content);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutManager.scrollToPositionWithOffset(15, dp2dx(50));
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }


    //轮播图片的适配器
    private class BitmapAdapter extends PagerAdapter {
        private List<String> datas;
        private Context context;

        public BitmapAdapter(List<String> datas, Context context) {
            this.datas = datas;
            this.context = context;
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(context);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            iv.setImageResource(R.mipmap.ic_launcher);
            return iv;
        }
    }

    //viewpager的显示动画

    private class ScaleInTransformer implements ViewPager.PageTransformer {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void transformPage(View view, float position) {
            view.setScaleX(0.999f);
            Log.d("position", "pos=" + position);
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            view.setPivotY(pageHeight / 2);
            view.setPivotX(pageWidth / 2);
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setScaleX(mMinScale);
                view.setScaleY(mMinScale);
                view.setPivotX(pageWidth);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                if (position < 0) //1-2:1[0,-1] ;2-1:1[-1,0]
                {

                    float scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    view.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));

                } else //1-2:2[1,0] ;2-1:2[0,1]
                {
                    float scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);
                    view.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));
                }
            } else { // (1,+Infinity]
                view.setPivotX(0);
                view.setScaleX(mMinScale);
                view.setScaleY(mMinScale);
            }
        }
    }

    public int dp2dx(int value) {
        return ((int) (getResources().getDisplayMetrics().density * value));
    }

    class StickyBean {
        String content;
        int type;

        public StickyBean(String content, int type) {
            this.content = content;
            this.type = type;
        }
    }
}
