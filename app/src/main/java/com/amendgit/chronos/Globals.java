package com.amendgit.chronos;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jash on 08/04/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */
public class Globals {

    final static String TAG = "Globals";

    private static SQLiteDatabase db;
    public static SQLiteDatabase getDatabase() {
        if (db != null) {
            return db;
        }
        db = SQLiteDatabase.openDatabase("/sdcard/amendgit/matrix.db", null, 0);
        if (db == null) {
            Log.d(TAG, "open data base failed.");
            return null;
        }
        db.execSQL("create table if not exists focus_events (id integer, startTime datetime, endTime datetime)");
        return null;
    }
}
