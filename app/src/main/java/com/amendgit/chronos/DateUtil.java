package com.amendgit.chronos;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

/**
 * Created by jash on 25/03/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */

class DateUtil {
    public static String millisecondToHHHMMSS(long millis) {
        int s = (int) ( millis /  1000)            % 60;
        int m = (int) ((millis / (1000 * 60))      % 60);
        int h = (int) ((millis / (1000 * 60 * 60)) % 24);
        return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
