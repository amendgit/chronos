package com.amendgit.chronos;

import java.util.Date;

/**
 * Created by jash on 08/04/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */
public class FocusEvent {
    private Date mStartTime;
    private Date mEndTime;
    private long mFocusInterval;

    public void setStartTime(Date startTime) {
        mStartTime = startTime;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setEndTime(Date endTime) {
        mEndTime = endTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setFocusInterval(long focusInterval) {
        mFocusInterval = focusInterval;
    }

    public long getFocusInterval() {
        return mFocusInterval;
    }
}
