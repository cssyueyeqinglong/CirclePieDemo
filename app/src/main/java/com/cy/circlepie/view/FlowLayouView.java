package com.cy.circlepie.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.circlepie.R;
import com.cy.circlepie.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 * 流式布局
 */

public class FlowLayouView extends ViewGroup {

    private int horizontalSpace = 10;
    private int verticalSpace = 10;
    List<LineBean> mLines = new ArrayList<>();
    private boolean isSameSpan;//默认情况下，标签是左对齐的，当为true的时候居中对齐

    public FlowLayouView(Context context) {
        super(context);
    }

    public FlowLayouView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public int getHorizontalSpace() {
        return horizontalSpace;
    }

    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    public int getVerticalSpace() {
        return verticalSpace;
    }

    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.flow_layou_view);
        horizontalSpace = (int) array.getDimension(R.styleable.flow_layou_view_horizontalSpace, 10);
        verticalSpace = (int) array.getDimension(R.styleable.flow_layou_view_verticalSpace, 10);
        array.recycle();
    }

    public FlowLayouView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initDatas() {
        for (int i = 0; i < 15; i++) {
            TextView tv = new TextView(getContext());
            tv.setPadding(UIUtils.dip2dx(getContext(), 8), UIUtils.dip2dx(getContext(), 4),
                    UIUtils.dip2dx(getContext(), 8), UIUtils.dip2dx(getContext(), 4));
            tv.setTextSize(14);
            tv.setBackgroundResource(R.drawable.item_text_bg);
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
        int tWidth = horizontalSpace;//总宽度
        int tHeight = verticalSpace;//总高度
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
                tWidth += horizontalSpace + width;
                tHeight += verticalSpace + height;
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
                    tHeight += height + verticalSpace;
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
        int top = verticalSpace;
        for (int i = 0; i < mLines.size(); i++) {
            LineBean lineBean = mLines.get(i);
            int left = horizontalSpace;
            for (int j = 0; j < lineBean.items.size(); j++) {
                View child = lineBean.items.get(j);
                if(isSameSpan&&j!=0){
                    left+=(getMeasuredWidth()-lineBean.width)/(lineBean.items.size()-1);
                }
                int right = left + child.getMeasuredWidth();
                int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                left += child.getMeasuredWidth() + horizontalSpace;
            }
            top += lineBean.height + verticalSpace;
        }
    }


    class LineBean {
        public List<View> items;
        public int width;
        public int height;
    }

    //是否均分多余的空格
    public void setSameSpan(boolean isSameSpan){
        this.isSameSpan=isSameSpan;
    }

}
