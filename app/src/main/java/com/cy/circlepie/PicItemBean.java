package com.cy.circlepie;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/7/13.
 */

public class PicItemBean {
    public Bitmap bitmap;
    public int bitmapId;
    public int itemId;

    @Override
    public String toString() {
        return "bitmap=" + bitmap.hashCode() + ",bitmapId==" + bitmapId + ",itemId=" + itemId;
    }
}
