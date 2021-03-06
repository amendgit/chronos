package com.amendgit.chronos;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by jash on 08/04/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */
public class FocusDAO {
    public static void insertEvent(FocusEvent event) {
        ContentValues record = new ContentValues();
        record.put("start_time", DateUtils.formatDate(event.getStartTime()));
        record.put("end_time", DateUtils.formatDate(event.getEndTime()));
        record.put("focus_interval", event.getFocusInterval());
        Globals.getDatabase().insert("focus_events", null, record);
    }

    public void getLatestEventDate() {
        // toimpl
    }

    public void getEventsByDate(Date date) {
        // toimpl
    }

    // [from, to)
    public void getEventsByDateRange(Date from, Date to) {
        // toimpl
    }
}
