package com.amendgit.chronos;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
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

    @Nullable
    public static SQLiteDatabase getDatabase() {
        if (db != null) {
            return db;
        }
        db = SQLiteDatabase.openOrCreateDatabase("/sdcard/amendgit/matrix.db", null);
        if (db == null) {
            Log.d(TAG, "open data base failed.");
            return null;
        }
        db.execSQL("create table if not exists focus_events ("
                + "id integer primary key asc,"
                + "start_time datetime,"
                + "end_time datetime,"
                + "focus_interval integer"
            +")");
        return db;
    }
}
