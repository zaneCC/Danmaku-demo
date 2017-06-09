package com.mgtv.danmaku.util;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * desc
 *
 * @author zhouzhan
 * @since 2017-05-18
 */
public abstract class RequestCountDownTimer {

    private static final String TAG = RequestCountDownTimer.class.getSimpleName();

    private CountDownTimer mTimer;
    private long mCurrent;
    private long mCountDownInterval;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public RequestCountDownTimer(final long millisInFuture, long countDownInterval) {
        mCurrent = millisInFuture;
        mCountDownInterval = countDownInterval;

        createNewTimer(millisInFuture, countDownInterval);
    }

    private void createNewTimer(final long millisInFuture, final long countDownInterval) {
        mTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrent = millisUntilFinished;
                RequestCountDownTimer.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                RequestCountDownTimer.this.onFinish();
            }
        };
    }

    public void onTick(long millisUntilFinished) {
        Log.e(TAG, "onTick: " + millisUntilFinished);
    }

    public void onFinish(){
    }

    public void resume(){
        createNewTimer(mCurrent, mCountDownInterval);
        start();
    }

    public void pause(){
        mTimer.cancel();
    }


    public void start(){
        mTimer.start();
    }

}
