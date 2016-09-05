package com.hypertian.library.framework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hypertian.library.R;

/**
 * Created by hypertian on 2016/8/24.
 * Desc
 */

public abstract class HTAbsTipProgressDialog extends Dialog {

    private RelativeLayout mRootView;
    private ImageView mIcon;
    private TextView mTitle, mMessage;

    protected HTAbsTipProgressDialog(Context context) {
        this(context, R.style.HTTipDialogStyle);
    }

    private HTAbsTipProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_progress);
        findViews();
    }

    private void findViews() {
        mRootView = (RelativeLayout) findViewById(R.id.tip_progress_dialog_root);
        mIcon = (ImageView) findViewById(R.id.iv_img_tip_progress_dialog);
        mTitle = (TextView) findViewById(R.id.tv_title_tip_progress_dialog);
        mMessage = (TextView) findViewById(R.id.tv_message_progress_dialog);
    }

    /**
     * 初始化
     */
    protected void init() {
        if (getIconId() != 0) {
            mIcon.setBackgroundResource(getIconId());
        } else {
            mIcon.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(getTitleMsg())) {
            mTitle.setText("提示");
        } else {
            mTitle.setText(getTitleMsg());
        }
    }

    public void setMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            mMessage.setText("确定?");
        } else {
            mMessage.setText(msg);
        }
    }

    public abstract int getIconId();

    public abstract String getTitleMsg();

    public void setBackground(String color) {
        mRootView.setBackgroundColor(Color.parseColor(color));
    }
}
