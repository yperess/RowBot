package com.concept2.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtils {
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Calendar parse(String date) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(sDateFormat.parse(date));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }

    public static String toDateString(Calendar calendar) {
        return calendar == null ? null : sDateFormat.format(calendar.getTime());
    }
}
