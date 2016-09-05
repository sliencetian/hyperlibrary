package com.hypertian.library.framework.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by hypertian on 2016/8/9.
 * Desc Activity的基类
 */

public class HTBaseActivity extends Activity {

    private int enterAnim;
    private int exitAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        if (intent != null) {
            enterAnim = intent.getIntExtra(HTActivityHelper.BUNDLE_KEY_ENTER_ANIM, android.R.anim.fade_in);
            exitAnim = intent.getIntExtra(HTActivityHelper.BUNDLE_KEY_EXIT_ANIM, android.R.anim.fade_out);
        }
        HTActivityHelper.getInstance().pushOneActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HTActivityHelper.getInstance().popOneActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(enterAnim, exitAnim);
    }
}
