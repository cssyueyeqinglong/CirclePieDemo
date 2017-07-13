package com.cy.circlepie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/7/13.
 */

public class RecyclerItemDiliver extends RecyclerView.ItemDecoration {

    private static final int[] arrs=new int[]{android.R.attr.listDivider};
    private final Drawable mDiliver;

    public RecyclerItemDiliver(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(arrs);
        mDiliver = typedArray.getDrawable(0);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c,parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            if(i==childCount-1){
                final int bottom = top;
                mDiliver.setBounds(left, top, right, bottom);
            }else {
                final int bottom = top + mDiliver.getIntrinsicHeight();
                mDiliver.setBounds(left, top, right, bottom);
            }

            mDiliver.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if(itemPosition==parent.getAdapter().getItemCount()-1){//最后一个
            outRect.set(0, 0, 0, 0);
        }else {
            outRect.set(0, 0, 0, mDiliver.getIntrinsicHeight());
        }

    }
}
