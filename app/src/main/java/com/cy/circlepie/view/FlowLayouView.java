package com.cy.circlepie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.circlepie.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FlowLayouView extends ViewGroup {

    private int mWidth;
    private int mHeight;
    private int horizontalSpace = 10;
    private int verticalSpace = 10;
    private int lineNum = 0;
    private String TAG = this.getClass().getSimpleName();
    List<LineBean> mLines = new ArrayList<>();
//    private LineBean currLine;

    public FlowLayouView(Context context) {
        super(context);
        initDatas(context);
    }

    public FlowLayouView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDatas(context);
    }

    public FlowLayouView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDatas(context);
    }

    private void initDatas(Context context) {
        for (int i = 0; i < 3; i++) {
            TextView tv = new TextView(context);
            tv.setPadding(UIUtils.dip2dx(context, 8), UIUtils.dip2dx(context, 4), UIUtils.dip2dx(context, 8), UIUtils.dip2dx(context, 4));
            tv.setTextSize(14);
            tv.setText("测试" + i);
            addView(tv);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeW = MeasureSpec.getMode(widthMeasureSpec);
        int specSizeW = MeasureSpec.getSize(widthMeasureSpec);
        int specModeH = MeasureSpec.getMode(heightMeasureSpec);
        int specSizeH = MeasureSpec.getSize(heightMeasureSpec);
        int tWidth = 0;//总宽度
        int tHeight=0;//总高度
        mLines.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);//先测量出孩子的尺寸
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            if (mLines.size() == 0) {//没有数据的时候，就添加第一行
                LineBean lineBean = new LineBean();
                lineBean.items = new ArrayList<>();
                lineBean.width = horizontalSpace + width;
                lineBean.height = height;
                lineBean.items.add(child);
                mLines.add(lineBean);
                tWidth = horizontalSpace + width;
                tHeight=verticalSpace+height;
            } else {//已经有了数据
                LineBean lineBean = mLines.get(mLines.size() - 1);//就先拿到当前最后一行的数据
                if (lineBean.width + width + horizontalSpace <= specSizeW) {//说明当前行还能在添加这个childView
                    lineBean.items.add(child);
                    lineBean.width += horizontalSpace + width;
                    lineBean.height = Math.max(lineBean.height, height);//取最高高度作为当前行的高度
                } else {//说明当前行不能在添加这个childView,那就新建一行，添加数据，就跟空数据时一样的操作
                    LineBean line = new LineBean();
                    line.items = new ArrayList<>();
                    line.width = horizontalSpace + width;
                    line.height = height;
                    line.items.add(child);
                    mLines.add(line);
                    tHeight+=height+verticalSpace;
                }
                tWidth = Math.max(lineBean.width, tWidth);//取得最大宽度值
            }
        }
        if (specModeW != MeasureSpec.EXACTLY) {
            specSizeW = tWidth;
        }
        if (specModeH != MeasureSpec.EXACTLY) {
            specSizeH = tHeight;
        }
        setMeasuredDimension(specSizeW, specSizeH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topS = verticalSpace;
        for (int i = 0; i < mLines.size(); i++) {
            LineBean lineBean = mLines.get(i);
            int leftS = horizontalSpace;
            for (int j = 0; j < lineBean.items.size(); j++) {
                View child = lineBean.items.get(j);
                int left = leftS;
                int top = topS;
                int right = leftS + child.getMeasuredWidth();
                int bottom = topS + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                leftS += child.getMeasuredWidth() + horizontalSpace;
            }
            topS += lineBean.height + verticalSpace;
        }
    }


    class LineBean {
        public List<View> items;
        public int width;
        public int height;
    }

}
