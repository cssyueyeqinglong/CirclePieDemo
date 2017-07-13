package com.cy.circlepie;

import android.content.Context;

/**
 * Created by Administrator on 2017/7/13.
 */

public class UIUtils {

    public static int dip2dx(Context context, int value) {
        return (int) (context.getResources().getDisplayMetrics().density * value + 0.5f);
    }
}
