package com.amendgit.chronos;

import android.os.CountDownTimer;

import java.util.ArrayList;

/**
 * Created by jash on 25/03/2018.
 */

public class FocusController {
    private String TAG = "FocusController";

    private CountDownTimer mCountDownTimer;
    private FocusState mState;
    private long mRemainTimerInterval;
    private long mTotalTimerInterval;
    private ArrayList<FocusDelegate> mDelegates;

    private FocusController() {
        mRemainTimerInterval = 0;
        mCountDownTimer = null;
        mState = FocusState.STOP;
        mDelegates = new ArrayList<>();
    }

    private static FocusController mInstance = null;
    public static FocusController getInstance() {
        if (mInstance == null) {
            synchronized (FocusController.class) {
                mInstance = new FocusController();
            }
        }
        return mInstance;
    }

    public void start(long totalTimeInterval) {
        this.setState(FocusState.RUN);
        mTotalTimerInterval = totalTimeInterval;
        mCountDownTimer = buildCountDownTimer(totalTimeInterval);
    }

    public void pause() {
        this.setState(FocusState.PAUSE);
        mCountDownTimer.cancel();
    }

    public void resume() {
        this.setState(FocusState.RUN);
        mCountDownTimer = buildCountDownTimer(mRemainTimerInterval);
    }

    public void stop() {
        this.setState(FocusState.STOP);
        mCountDownTimer.cancel();
    }

    private CountDownTimer buildCountDownTimer(long remainTimeInterval) {
        return new CountDownTimer(remainTimeInterval, 32) {
            @Override
            public void onTick(long l) {
                mRemainTimerInterval = l;
                for (FocusDelegate delegate : mDelegates) {
                    delegate.onTick(l);
                }
            }

            @Override
            public void onFinish() {
                for (FocusDelegate delegate : mDelegates) {
                    delegate.onFinish();
                }
            }
        }.start();
    }

    private void setState(FocusState state) {
        mState = state;
        for (FocusDelegate delegate : mDelegates) {
            delegate.onStateChange(state);
        }
    }

    public FocusState getState() {
        return mState;
    }

    public long getTotalTimeInterval() {
        return mTotalTimerInterval;
    }

    public void addDelegate(FocusDelegate delegate) {
        mDelegates.add(delegate);
    }

    public interface FocusDelegate {
        void onTick(long remainMillis);
        void onFinish();
        void onStateChange(FocusState state);
    }
}
