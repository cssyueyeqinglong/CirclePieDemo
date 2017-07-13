package com.cy.circlepie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cy.circlepie.PieItemBean;
import com.cy.circlepie.UIUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

/**
 * Created by cy on 2017/6/27.
 * 自定义饼状图
 */

public class CirclePieView extends View {
    private Paint mPaint;//画扇形的paint
    private Paint mPaintBorder;//画扇形边框的paint
    private int mRadious;//圆的半径
    private List<PieItemBean> mDatas = new ArrayList<>();
    //    private float mOffsetAngle;//起始偏移角度
    private float totalAngle = 0f;
    private RectF mRectF;
    private float[] degrees;
    private int selectedPos;//被选中的条目角标
    private int cenY;
    private int cenX;

    public CirclePieView(Context context) {
        super(context);
        initPaint();
    }

    public CirclePieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CirclePieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setColor(Color.BLACK);
        mPaintBorder.setTextSize(35);

        mDatas.add(new PieItemBean("测试1", 9, Color.rgb(155, 187, 90)));
        mDatas.add(new PieItemBean("测试2", 3, Color.rgb(191, 79, 75)));
        mDatas.add(new PieItemBean("测试3", 76f, Color.rgb(242, 167, 69)));
        mDatas.add(new PieItemBean("测试4", 6, Color.rgb(60, 173, 213)));
        mDatas.add(new PieItemBean("测试5", 6, Color.rgb(90, 79, 88)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureValue(widthMeasureSpec), measureValue(heightMeasureSpec));
    }

    private int measureValue(int measureSpec) {
        int result = UIUtils.dip2dx(getContext(),250);//设置最小值
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) { //fill_parent或者设置了具体的宽高
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int rWidth = width - getPaddingLeft() - getPaddingRight();
        int rHeight = height - getPaddingTop() - getPaddingBottom();
        mRadious = Math.min(rWidth, rHeight)*1 / 3;
        cenX = Math.min(rWidth, rHeight)/2;
        cenY = cenX;
        //用于存放当前百分比的圆心角度
        float currentAngle = 0.0f;
        float offsetAngle = 0f;
        degrees = new float[mDatas.size()];
        for (int i = 0; i < mDatas.size(); i++) {
            PieItemBean bean = mDatas.get(i);
            currentAngle = per2Radious(totalAngle, bean.value);//得到当前角度
            mPaint.setColor(bean.color);//给画笔设置颜色
            if (mRectF == null) {//设置圆所需的范围
                mRectF = new RectF(cenX-mRadious, cenX-mRadious, cenX+mRadious, cenX+mRadious);
            }
            if (selectedPos == i) {//选中的偏离一点儿
                canvas.save();
                canvas.translate(cenX, cenY);//将画布平移到圆心
                canvas.rotate(offsetAngle + currentAngle / 2);//先将画布x轴旋转到当前角度的一半位置，
                canvas.translate(20, 0);//然后在平移50个单位，就将被选中的模块独立出来了
                RectF rcf = new RectF(-mRadious, -mRadious, mRadious, mRadious);
                canvas.drawArc(rcf, currentAngle / 2, -currentAngle, true, mPaint);
                //边框
                canvas.drawArc(rcf, currentAngle / 2, -currentAngle, true, mPaintBorder);
                canvas.restore();
            } else {
                //在饼图中显示所占比例
                canvas.drawArc(mRectF, offsetAngle, currentAngle, true, mPaint);
                //边框
                canvas.drawArc(mRectF, offsetAngle, currentAngle, true, mPaintBorder);
            }
            //先将度数定位到当前所在条目的一半位置
            float degree = offsetAngle + currentAngle / 2;
            //根据角度所在不同象限来计算出文字的起始点坐标
            float dx = 0, dy = 0;
            if (degree > 0 && degree <= 90f) {//在第四象限
                dx = (float) (cenX + mRadious * 2 / 3 * Math.cos(2 * PI / 360 * degree));//注意Math.sin(x)中x为弧度值，并非数学中的角度，所以需要将角度转换为弧度
                dy = (float) (cenY + mRadious * 2.5 / 3 * Math.sin(2 * PI / 360 * degree));
            } else if (degree > 90f && degree <= 180f) {//在第三象限
                dx = (float) (cenX - mRadious * 2 / 3 * Math.cos(2 * PI / 360 * (180f - degree)));
                dy = (float) (cenY + mRadious * 2.5 / 3 * Math.sin(2 * PI / 360 * (180f - degree)));
            } else if (degree > 180f && degree <= 270f) {//在第二象限
                dx = (float) (cenX - mRadious * 2 / 3 * Math.cos(2 * PI / 360 * (270f - degree)));
                dy = (float) (cenY - mRadious * 2.5 / 3 * Math.sin(2 * PI / 360 * (270f - degree)));
            } else {
                dx = (float) (cenX + mRadious * 2 / 3 * Math.cos(2 * PI / 360 * (360f - degree)));
                dy = (float) (cenY - mRadious * 2.5 / 3 * Math.sin(2 * PI / 360 * (360f - degree)));
            }
            //文字的基本线坐标设置为半径的2.3/3位置处，起点y坐标设置为半径的2.7/3位置处
            canvas.drawText(bean.value + "%", dx, dy, mPaintBorder);
            //下次的起始角度
            offsetAngle += currentAngle;
            degrees[i] = offsetAngle;
        }
        mHander.sendEmptyMessageDelayed(MSG_INFO, 100);
    }

    private static final int MSG_INFO = 0x24;
    private int num = 0;//总共扩大次数
    private float perDegree = 360f / 10;//单位扩大角度
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_INFO) {
                if (num > 10) {
                    return;
                }
                totalAngle = num * perDegree;
                invalidate();
                num++;
            }
        }
    };

    private long startTime;//点击的起始时间

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                long currTime = System.currentTimeMillis();
                float dx = x - cenX;
                float dy = y - cenY;
                if (currTime - startTime <= 500) {//按下和抬起的时间在500毫秒内认为是单击
                    float degree;//被点击选中的角度
                    //先判断是否在圆内部
                    if (isInCircle(dx, dy, mRadious)) {//根据不同的象限来获取到x轴的角度
                        if (dx > 0 && dy > 0) {//第四象限
                            degree = (float) (90f - 180 * Math.atan2(dx, dy) / PI);
                        } else if (dx < 0 && dy > 0) {//第三象限
                            degree = (float) (180 * Math.atan2(-dx, dy) / PI + 90f);
                        } else if (dx < 0 && dy < 0) {//第二象限
                            degree = (float) (180 * Math.atan2(dy, dx) / PI + 360f);
                        } else {//第一象限
                            degree = (float) (360f - 180 * Math.atan2(-dy, dx) / PI);
                        }
                        //然后判断该角度在哪个数据集内
                        selectedPos = judgeDegree(degree);
                        invalidate();//请求重绘
                    }
                }
                return false;
        }
        return true;
    }

    /**
     * 根据坐标计算是否在圆内部
     */
    private boolean isInCircle(float lx, float ly, float radius) {
        double v = Math.pow(Math.abs(lx), 2) + Math.pow(Math.abs(ly), 2);
        double dis = Math.sqrt(v);
        if (dis > radius) {
            return false;
        }
        return true;
    }

    /**
     * 判断当前点击的在哪个数据集合里面
     */
    private int judgeDegree(float degree) {
        int selectedPos = 0;
        for (int i = 0; i < degrees.length; i++) {
            if (degree <= degrees[i]) {
                selectedPos = i;
                break;
            }
        }
        return selectedPos;
    }

    /**
     * 将百分比转换为图心角角度
     */
    public float per2Radious(float totalAngle, float percentage) {
        float angle = 0.0f;
        if (percentage >= 101f || percentage < 0.0f) {
            //Log.e(TAG,"输入的百分比不合规范.须在0~100之间.");
        } else {
            float v = percentage / 100;//先获取百分比
            float itemPer = totalAngle * v;//获取对应角度的百分比
            angle = round(itemPer, 2);//精确到小数点后面2位
        }
        return angle;
    }

    /**
     * 四舍五入到小数点后scale位
     */
    public float round(float v, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        BigDecimal bgNum1 = new BigDecimal(v);
        BigDecimal bgNum2 = new BigDecimal("1");
        return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
