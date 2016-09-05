package com.hypertian.library.framework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hypertian.library.util.HTLog;

import java.util.Stack;

/**
 * Created by hypertian on 2016/8/9.
 * Desc     Activity助手类，封装了一些对于Activity的常见操作
 */

public class HTActivityHelper {


    public static final int ANIM_FADE_IN = 108;
    public static final int ANIM_FADE_OUT = 109;

    public static final String BUNDLE_KEY_ENTER_ANIM = "activity_return_enterAnim";
    public static final String BUNDLE_KEY_EXIT_ANIM = "activity_return_exitAnim";

    private static final String TAG = HTActivityHelper.class.getSimpleName();

    private static Stack<Activity> activityStack = new Stack<Activity>();

    /*================     以下是对Activity跳转的相关操作     =======================*/

    public static void showActivity(Activity mFromActivity, Class<?> mToActivityClass) {
        showActivity(mFromActivity, mToActivityClass, null, null, ANIM_FADE_IN, ANIM_FADE_OUT);
    }

    public static void showActivity(Activity mFromActivity, Class<?> mToActivityClass, Bundle mBundle) {
        showActivity(mFromActivity, mToActivityClass, mBundle, null, ANIM_FADE_IN, ANIM_FADE_OUT);
    }

    public static void showActivity(Activity mFromActivity, Class<?> mToActivityClass, Bundle mBundle,
                                    String mAction) {
        showActivity(mFromActivity, mToActivityClass, mBundle, mAction, ANIM_FADE_IN, ANIM_FADE_OUT);
    }

    public static void showActivity(Activity mFromActivity, Class<?> mToActivityClass, Bundle mBundle,
                                    String mAction, int mEnterAnim) {
        showActivity(mFromActivity, mToActivityClass, mBundle, mAction, mEnterAnim, ANIM_FADE_OUT);
    }

    private static int _getReverseAnimRes(int animType) {
        int res = -1;

        switch (animType) {
            case ANIM_FADE_IN:
                res = android.R.anim.fade_in;
                break;
            case ANIM_FADE_OUT:
                res = android.R.anim.fade_out;
                break;
            default:
                res = 0;
                break;
        }

        return res;
    }


    private static void showActivity(Activity mFromActivity, Class<?> mToActivityClass, Bundle mBundle,
                                     String mAction, int mEnterAnim, int mExitAnim) {
        if (null == mFromActivity || mFromActivity.isFinishing()) {
            return;
        }

        Intent intent = new Intent(mFromActivity, mToActivityClass);

        if (null == mBundle) {
            mBundle = new Bundle();
        }
        mBundle.putInt(BUNDLE_KEY_ENTER_ANIM, _getReverseAnimRes(mEnterAnim));
        mBundle.putInt(BUNDLE_KEY_EXIT_ANIM, _getReverseAnimRes(mExitAnim));
        intent.putExtras(mBundle);

        if (!TextUtils.isEmpty(mAction)) {
            intent.setAction(mAction);
        }

        mFromActivity.startActivity(intent);

        mFromActivity.overridePendingTransition(_getReverseAnimRes(mEnterAnim), _getReverseAnimRes(mExitAnim));
    }

    /*================     以下是对Activity栈的相关操作     =======================*/

    /**
     * 将当前Activity推入栈中
     *
     * @param activity Activity
     */
    void pushOneActivity(Activity activity) {
        if (null != activity) {
            activityStack.push(activity);
            HTLog.i(TAG, "PushActivity = " + activity.getLocalClassName());
            HTLog.i(TAG, "ActivityStack.size = " + activityStack.size());
        }
    }

    /**
     * 退出Activity
     *
     * @param activity Activity
     */
    public void popOneActivity(Activity activity) {
        if (null != activity) {
            if (activityStack.remove(activity)) {
                activity.finish();
                HTLog.i(TAG, "PopActivity = " + activity.getLocalClassName());
                HTLog.i(TAG, "ActivityStack.size = " + activityStack.size());
            }
        }
    }

    /**
     * 退出Activity
     *
     * @param activityContext activityContext
     */
    public void popOneActivity(Context activityContext) {
        if (null != activityContext) {
            try {
                HTBaseActivity activity = (HTBaseActivity) activityContext;
                if (activityStack.remove(activity)) {
                    activity.finish();
                    HTLog.i(TAG, "PopActivity = " + activity.getLocalClassName());
                    HTLog.i(TAG, "ActivityStack.size = " + activityStack.size());
                }
            } catch (ClassCastException e) {
                throw new ClassCastException("activityContext is not HTBaseActivity's context");
            }
        }
    }

    /**
     * 退出栈中其他所有Activity
     *
     * @param cls Class 类名
     */
    public void popOtherActivity(Class cls) {
        if (cls == null) {
            throw new UnsupportedOperationException("this class is null");
        }
        for (Activity activity : activityStack) {
            if (null == activity || activity.getClass().equals(cls)) {
                continue;
            }
            activity.finish();
        }
        HTLog.i(TAG, "ActivityStack.size = " + activityStack.size());
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity() {
        if (null != activityStack) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (null == activity)
                    break;
                popOneActivity(activity);
            }
        }
    }

    /**
     * 获取栈顶的activity，先进后出原则
     *
     * @return
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * =============================     单例模式    ===========================
     */
    private HTActivityHelper() {
    }

    private static class ActivityHolder {
        private static final HTActivityHelper INSTANCE = new HTActivityHelper();
    }

    public static final HTActivityHelper getInstance() {
        return ActivityHolder.INSTANCE;
    }
}
