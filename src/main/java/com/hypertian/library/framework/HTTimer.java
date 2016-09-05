package com.hypertian.library.framework;

import android.os.Handler;

/**
 * Created by hypertian on 2016/8/19.
 * Desc
 */

public class HTTimer {

    public interface HTTimerCallBack {
        void timeTicked(HTTimer srcTimer);
    }

    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private HTTimerCallBack timerCallBack = null;
    private float interval = 1.0f;//时间间隔 默认为1S
    private int roundCount = -1;//总轮循次数
    public int currRoundCount = 0;//当前轮循次数
    private boolean isRunning = false;

    public HTTimer(HTTimerCallBack callBack) {
        timerCallBack = callBack;
        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                currRoundCount++;
                mHandler.postDelayed(mRunnable, (long) interval * 1000);
                if (timerCallBack != null) {
                    timerCallBack.timeTicked(HTTimer.this);
                }
                if(roundCount != -1 && currRoundCount == roundCount){
                    stopTimer();
                }
            }
        };
    }

    /**
     * 开启计时器,每second秒回调一次
     */
    public void startTimer(float seconds) {
        interval = seconds;
        mHandler.postDelayed(mRunnable, (long) (interval * 1000));
        isRunning = true;
    }

    /**
     * 开启计时器
     * @param seconds 每second秒回调一次
     * @param roundCount 一共轮循roundCount次
     */
    public void startTimer(float seconds,int roundCount){
        this.roundCount = roundCount;
        startTimer(seconds);
    }

    /**
     * 关闭计时器
     */
    public void stopTimer() {
        if (!isRunning) {
            return;
        }
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler = null;
        mRunnable = null;
        timerCallBack = null;
        currRoundCount = 0;
        isRunning = false;
    }

    /**
     * 当前计时器是否正在执行
     */
    public boolean timerIsRunning() {
        return isRunning;
    }

}
