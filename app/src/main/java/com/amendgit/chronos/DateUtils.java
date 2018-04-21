package com.amendgit.chronos;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.text.format.DateFormat.getBestDateTimePattern;
import static java.util.Calendar.*;
import java.util.*;

/**
 * Created by jash on 25/03/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */

class DateUtils {
    /**
     * Number of milliseconds in one day.
     */
    public static long millisecondsInOneDay = 24 * 60 * 60 * 1000;

    private static Long fixedLocalTime = null;

    private static TimeZone fixedTimeZone = null;

    public static String millisecondToHHHMMSS(long millis) {
        int s = (int) ( millis /  1000)            % 60;
        int m = (int) ((millis / (1000 * 60))      % 60);
        int h = (int) ((millis / (1000 * 60 * 60)) % 24);
        return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }

    /**
     * @return array with weekday names starting according to locale settings,
     * e.g. [Mo,Di,Mi,Do,Fr,Sa,So] in Europe
     */
    public static String[] getLocaleDayNames(int format)
    {
        String[] days = new String[7];

        Calendar calendar = new GregorianCalendar();
        calendar.set(DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        for (int i = 0; i < days.length; i++)
        {
            days[i] = calendar.getDisplayName(DAY_OF_WEEK, format,
                    Locale.getDefault());
            calendar.add(DAY_OF_MONTH, 1);
        }

        return days;
    }

    public static long getLocalTime() {
        if (fixedLocalTime != null) return fixedLocalTime;

        TimeZone tz = getTimezone();
        long now = new Date().getTime();
        return now + tz.getOffset(now);
    }

    public static long getStartOfDay(long timestamp) {
        return (timestamp / millisecondsInOneDay) * millisecondsInOneDay;
    }

    public static long getStartOfToday() {
        return getStartOfDay(DateUtils.getLocalTime());
    }

    public static GregorianCalendar getStartOfTodayCalendar() {
        return getCalendar(getStartOfToday());
    }

    public static TimeZone getTimezone() {
        if (fixedTimeZone != null) return fixedTimeZone;
        return TimeZone.getDefault();
    }

    public static GregorianCalendar getCalendar(long timestamp) {
        GregorianCalendar day =
                new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        day.setTimeInMillis(timestamp);
        return day;
    }

    @NonNull
    private static SimpleDateFormat fromSkeleton(@NonNull String skeleton,
                                                 @NonNull Locale locale)
    {
        SimpleDateFormat df = new SimpleDateFormat(skeleton, locale);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df;
    }

    @NonNull
    public static SimpleDateFormat fromSkeleton(@NonNull String skeleton)
    {
        Locale locale = Locale.getDefault();

        if (SDK_INT >= JELLY_BEAN_MR2)
            skeleton = getBestDateTimePattern(locale, skeleton);

        return fromSkeleton(skeleton, locale);
    }
}
