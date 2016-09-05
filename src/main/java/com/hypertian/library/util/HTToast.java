package com.hypertian.library.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by hypertian on 2016/8/12.
 * Desc Toast提示工具
 */

public class HTToast {

    private static int ONE_SECOND = 1000;
    private static int TWO_SECOND = 2000;

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, duration);
        mToast.show();
    }


    public static void showToastShort(Context mContext, String text) {
        showToast(mContext, text, ONE_SECOND);
    }

    public static void showToastLong(Context mContext, String text) {
        showToast(mContext, text, TWO_SECOND);
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

    public static void showToastShort(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId), ONE_SECOND);
    }

    public static void showToastLong(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId), TWO_SECOND);
    }

}
