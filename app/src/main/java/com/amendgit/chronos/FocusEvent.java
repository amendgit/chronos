package com.amendgit.chronos;

import java.util.Date;

/**
 * Created by jash on 08/04/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */
public class FocusEvent {
    private Date startTime;
    private Date endTime;

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
