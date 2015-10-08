package com.vuduc.android.worksoptimization.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by vuduc on 8/26/15.
 */
public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    public static String hour2Text(long millis) {
//        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
//                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
//                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1));
    }

    public static String date2Text(Date date) {
        return date2Text(date, "dd-MM-yyyy");
    }

    public static String date2Text(long millis) {
        return date2Text(millis, "dd-MM-yyyy");
    }

    public static String date2Text(Date date, String formatString) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatString, Locale.US);
        return formatter.format(date);
    }

    public static String date2Text(long millis, String formatString) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatString, Locale.US);
        Date date = new Date(millis);
        return formatter.format(date);
    }

    public static Date string2Date(String source) {
        return string2Date(source, "dd-MM-yyyy");
    }

    public static Date string2Date(String source, String formatString) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatString);
        Date date = null;
        try {
            date = formatter.parse(source);
        } catch (NullPointerException | ParseException e) {
            date = new Date();
        }
        return date;
    }

    public static int[] millis2Time(Long millis) {
        int h = (int) TimeUnit.MILLISECONDS.toHours(millis);
        int m = (int) (TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1));
        return new int[]{h, m};
    }
}
