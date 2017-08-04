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
    private RecyclerView mRecyclerView;
    private String[] data;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final StickyItemDection floatingItemDecoration = new StickyItemDection(this);
        Map<Integer, String> keys = new HashMap<>();
        keys.put(3, "呵呵");
        floatingItemDecoration.setKeys(keys);
        floatingItemDecoration.setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
        mRecyclerView.addItemDecoration(floatingItemDecoration);

        mRecyclerView.setAdapter(new ViewAdapter());
        int itemCount = mRecyclerView.getAdapter().getItemCount();
        Log.d("count", "count:" + itemCount);
    }

    private class ViewAdapter extends RecyclerView.Adapter<TestViewHolder> {

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(ViewPagerActivity.this);
            tv.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2dx(50));
            tv.setLayoutParams(params);
            return new TestViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            ((TextView) holder.itemView).setText("位置：" + position);
        }

        @Override
        public int getItemCount() {
            return data.length * 6;
        }
    }

    private class TestViewHolder extends RecyclerView.ViewHolder {

        public TestViewHolder(View itemView) {
            super(itemView);
        }
    }

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
}
