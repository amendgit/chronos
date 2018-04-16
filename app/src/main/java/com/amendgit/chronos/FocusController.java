package com.amendgit.chronos;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jash on 25/03/2018.
 */

public class FocusController {
    private String TAG = "FocusController";

    private CountDownTimer mCountDownTimer;
    private FocusState mState;

    private long mRemainTimerInterval;
    private long mTotalTimerInterval;
    private Date mStartTime;

    private ArrayList<FocusDelegate> mDelegates;

    private FocusController() {
        mRemainTimerInterval = 0;
        mCountDownTimer = null;
        mState = FocusState.READY;
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
        this.setState(FocusState.FOCUSING);
        mStartTime = Calendar.getInstance().getTime();
        mTotalTimerInterval = totalTimeInterval;
        mCountDownTimer = buildCountDownTimer(totalTimeInterval);
    }

    public void pause() {
        this.setState(FocusState.SUSPENDING);
        mCountDownTimer.cancel();
    }

    public void resume() {
        this.setState(FocusState.FOCUSING);
        mCountDownTimer = buildCountDownTimer(mRemainTimerInterval);
    }

    public void stop() {
        this.setState(FocusState.READY);
        mCountDownTimer.cancel();
    }

    private CountDownTimer buildCountDownTimer(long remainTimeInterval) {
        return new CountDownTimer(remainTimeInterval, 32) {
            @Override
            public void onTick(long l) {
                mRemainTimerInterval = l;
                for (FocusDelegate delegate : mDelegates) {
                    delegate.onTickTock(l);
                }
            }

            @Override
            public void onFinish() {
                FocusEvent event = new FocusEvent();
                event.setStartTime(mStartTime);
                event.setEndTime(Calendar.getInstance().getTime());
                event.setFocusInterval(mTotalTimerInterval);
                FocusDAO.insertEvent(event);
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
        void onTickTock(long remainMillis);
        void onFinish();
        void onStateChange(FocusState state);
    }
}
