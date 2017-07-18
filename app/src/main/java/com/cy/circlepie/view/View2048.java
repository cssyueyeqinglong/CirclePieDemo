package com.cy.circlepie.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/18.
 * 2048小游戏
 */

public class View2048 extends GridLayout {
    private static final int TYPE = 4;
    private String TAG = this.getClass().getSimpleName();
    private int[] colors = new int[]{0x11ff0000, 0x1100ff00, 0x110000ff, 0x11ffff00};
    private int[] values = new int[]{2, 4, 8};
    private Context mContext;
    private Random mRandom;
    private int[] mValues = new int[TYPE * TYPE];
    private int target = 2048;
    private int totalNum = 0;
    private int flag = 0;

    public View2048(Context context) {
        super(context, null);
        init(context);
    }

    public View2048(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public View2048(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setColumnCount(TYPE);
        setRowCount(TYPE);
        setBackgroundColor(Color.GREEN);
        mContext = context;
        mRandom = new Random();
        addViews();
        addRandowItem();
        addRandowItem();
    }

    public void addViews() {
        for (int i = 0; i < TYPE * TYPE; i++) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dip2dx(40), dip2dx(40));
            TextView tv = new TextView(mContext);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(15);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(dip2dx(2), dip2dx(2), dip2dx(2), dip2dx(2));
            tv.setBackgroundColor(Color.GRAY);
            tv.setLayoutParams(params);
            mValues[i] = (0);
            addView(tv);
        }
    }

    /**
     * 任意添加一个item
     */
    public void addRandowItem() {
        int pos = mRandom.nextInt(TYPE * TYPE);
        if (mValues[pos] == 0) {
            int data = values[mRandom.nextInt(3)];
            mValues[pos] = data;
            ((TextView) getChildAt(pos)).setText("" + data);
        } else {
            if (totalNum >= TYPE * TYPE) {
                if (!couldMove()) {
                    Toast.makeText(mContext, "您已无路可走！！", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            addRandowItem();
        }
    }

    /**
     * 判断是否还可以移动
     *
     * @return
     */
    public boolean couldMove() {
        for (int i = 0; i < TYPE; i++) {
            int temp = 0;
            int columTemp = 0;
            for (int j = 0; j < TYPE; j++) {
                //先纵行判断
                int rowValue = mValues[i + j * TYPE];
                if (temp == rowValue) {
                    return true;
                }
                temp = rowValue;
                //再横行判断
                int columValue = mValues[i * TYPE + j];
                if (columTemp == columValue) {
                    return true;
                }
                columTemp = columValue;
            }
        }
        return false;
    }

    private float startx, starty;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = x;
                starty = y;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float dx = x - startx;
                float dy = y - starty;
                if (Math.abs(dx) > Math.abs(dy)) {//x轴滑动
                    if (dx > 0) {//向右
                        Log.e(TAG, "onTouchEvent: 向右");
                        flag = 4;
                        moveRight();
                    } else {//向左
                        flag = 3;
                        Log.e(TAG, "onTouchEvent: 向左");
                        moveLeft();
                    }
                } else {//y轴滑动
                    if (dy > 0) {//向下
                        flag = 2;
                        Log.e(TAG, "onTouchEvent: 向下");
                        moveDown();
                    } else {//向上
                        flag = 1;
                        Log.e(TAG, "onTouchEvent: 向上");
                        moveUp();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 右滑
     */
    private void moveRight() {
        totalNum = 0;
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < TYPE; i++) {
            datas.clear();
            int temp = 0;
            for (int j = TYPE - 1; j >= 0; j--) {
                int value = mValues[i * TYPE + j];
                if (value == 0) continue;
                if (temp == value) {
                    temp = 0;
                    datas.set(datas.size() - 1, value * 2);
                } else {
                    temp = value;
                    datas.add(temp);
                }
            }
            totalNum += datas.size();
            for (int j = 0; j < TYPE; j++) {
                if (datas.size() - 1 >= j) {
                    mValues[i * TYPE + (TYPE - 1 - j)] = datas.get(j);
                } else {
                    mValues[i * TYPE + (TYPE - 1 - j)] = 0;
                }
            }
        }
        addRandowItem();
        for (int i = 0; i < TYPE * TYPE; i++) {
            ((TextView) getChildAt(i)).setText(mValues[i] == 0 ? "" : "" + mValues[i]);
            if (mValues[i] == target) {
                target *= 2;
                Toast.makeText(mContext, "您通关啦,还可以继续挑战更高分", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 左滑
     */
    private void moveLeft() {
        totalNum = 0;
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < TYPE; i++) {
            datas.clear();
            int temp = 0;
            for (int j = 0; j < TYPE; j++) {
                int value = mValues[i * TYPE + j];
                if (value == 0) continue;
                if (temp == value) {
                    temp = 0;
                    datas.set(datas.size() - 1, value * 2);
                } else {
                    temp = value;
                    datas.add(temp);
                }
            }
            totalNum += datas.size();
            for (int j = 0; j < TYPE; j++) {
                if (datas.size() - 1 >= j) {
                    mValues[i * TYPE + j] = datas.get(j);
                } else {
                    mValues[i * TYPE + j] = 0;
                }
            }
        }
        addRandowItem();
        for (int i = 0; i < TYPE * TYPE; i++) {
            ((TextView) getChildAt(i)).setText(mValues[i] == 0 ? "" : "" + mValues[i]);
        }

    }

    /**
     * 下滑
     */
    private void moveDown() {
        totalNum = 0;
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < TYPE; i++) {
            datas.clear();
            int temp = 0;
            for (int j = TYPE - 1; j >= 0; j--) {
                int value = mValues[i + TYPE * j];
                if (value == 0) continue;
                if (temp == value) {
                    temp = 0;
                    datas.set(datas.size() - 1, value * 2);
                } else {
                    temp = value;
                    datas.add(temp);
                }
            }
            totalNum += datas.size();
            for (int j = 0; j < TYPE; j++) {
                if (datas.size() - 1 >= j) {
                    mValues[i + TYPE * (TYPE - 1 - j)] = datas.get(j);
                } else {
                    mValues[i + TYPE * (TYPE - 1 - j)] = 0;
                }
            }
        }
        addRandowItem();
        for (int i = 0; i < TYPE * TYPE; i++) {
            ((TextView) getChildAt(i)).setText(mValues[i] == 0 ? "" : "" + mValues[i]);
        }

    }

    /**
     * 上滑
     */
    private void moveUp() {
        totalNum = 0;
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < TYPE; i++) {
            datas.clear();
            int temp = 0;
            for (int j = 0; j < TYPE; j++) {
                int value = mValues[i + TYPE * j];
                if (value == 0) continue;
                if (temp == value) {
                    temp = 0;
                    datas.set(datas.size() - 1, value * 2);
                } else {
                    temp = value;
                    datas.add(temp);
                }
            }
            totalNum += datas.size();
            for (int j = 0; j < TYPE; j++) {
                if (datas.size() - 1 >= j) {
                    mValues[i + TYPE * j] = datas.get(j);
                } else {
                    mValues[i + TYPE * j] = 0;
                }
            }
        }
        addRandowItem();
        for (int i = 0; i < TYPE * TYPE; i++) {
            ((TextView) getChildAt(i)).setText(mValues[i] == 0 ? "" : "" + mValues[i]);
        }

    }

    public int dip2dx(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().density * value + 0.5f);
    }


}
