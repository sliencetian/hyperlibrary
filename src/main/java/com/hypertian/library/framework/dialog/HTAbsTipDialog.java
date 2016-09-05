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
 * Created by hypertian on 2016/8/23.
 * Desc
 */

public abstract class HTAbsTipDialog extends Dialog {

    private RelativeLayout mRootView;
    private ImageView mIcon;
    private TextView mTitle, mMessage, mConfirm, mCancel;

    public HTAbsTipDialog(Context context) {
        this(context, R.style.HTTipDialogStyle);
    }

    private HTAbsTipDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_result);
        findViews();
    }

    private void findViews() {
        mRootView = (RelativeLayout) findViewById(R.id.tip_dialog_root);
        mIcon = (ImageView) findViewById(R.id.iv_img_tip_dialog);
        mTitle = (TextView) findViewById(R.id.tv_title_tip_dialog);
        mMessage = (TextView) findViewById(R.id.tv_msg_tip_dialog);
        mConfirm = (TextView) findViewById(R.id.tv_left_dialog);
        mCancel = (TextView) findViewById(R.id.tv_right_dialog);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (getDialogListener() != null)
                    getDialogListener().confirm();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (getDialogListener() != null)
                    getDialogListener().cancel();
            }
        });
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
        if (TextUtils.isEmpty(getMsg())) {
            mMessage.setText("确定?");
        } else {
            mMessage.setText(getMsg());
        }
        if (TextUtils.isEmpty(getConfirm())) {
            mConfirm.setText("确定");
        } else {
            mConfirm.setText(getConfirm());
        }
        if (TextUtils.isEmpty(getCancel())) {
            mCancel.setText("取消");
        } else {
            mCancel.setText(getCancel());
        }
    }


    public abstract int getIconId();

    public abstract String getTitleMsg();

    public abstract String getMsg();

    public abstract String getConfirm();

    public abstract String getCancel();

    public abstract IDialogListener getDialogListener();

    public void setBackground(String color) {
        mRootView.setBackgroundColor(Color.parseColor(color));
    }

}
