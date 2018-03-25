package com.amendgit.chronos;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jash on 25/03/2018.
 */

public class FocusController {
    private String TAG = "FocusController";

    private CountDownTimer mCountDownTimer;
    private FocusState mState;
    private long mRemainMillis;
    private long mTotalMillis;
    private ArrayList<FocusDelegate> mDelegates;

    private FocusController() {
        mRemainMillis = 0;
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
        mState = FocusState.RUN;
        mTotalMillis = totalTimeInterval;
        mCountDownTimer = buildCountDownTimer(totalTimeInterval);
    }

    public void pause() {
        mState = FocusState.PAUSE;
        mCountDownTimer.cancel();
    }

    public void resume() {
        mState = FocusState.RUN;
        mCountDownTimer = buildCountDownTimer(mRemainMillis);
    }

    private CountDownTimer buildCountDownTimer(long remainTimeInterval) {
        return new CountDownTimer(remainTimeInterval, 32) {
            @Override
            public void onTick(long l) {
                mRemainMillis = l;
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

    public void stop() {
        mState = FocusState.STOP;
        mCountDownTimer.cancel();
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

    public long getTotalMillis() {
        return mTotalMillis;
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
