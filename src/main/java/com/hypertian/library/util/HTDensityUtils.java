package com.hypertian.library.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by hypertian on 2016/8/15.
 * Desc  常用单位转换的辅助类
 */
public class HTDensityUtils {
    /*构造函数私有化*/
    private HTDensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, int pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}