package com.amendgit.chronos;

import java.util.Locale;

/**
 * Created by jash on 25/03/2018.
 */

public class DateUtil {
    public static String millisecondToHHHMMSS(long millis) {
        int s = (int) ( millis /  1000)            % 60;
        int m = (int) ((millis / (1000 * 60))      % 60);
        int h = (int) ((millis / (1000 * 60 * 60)) % 24);
        return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
    }
}
