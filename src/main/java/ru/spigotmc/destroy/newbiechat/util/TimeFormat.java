package ru.spigotmc.destroy.newbiechat.util;

import java.text.SimpleDateFormat;

public class TimeFormat {

    private final long time;

    private final String hour = "hhч.";
    private final String min = "mmм.";
    private final String sec = "ssсек.";

    private final String nullHour = replace(hour, "hh");
    private final String nullMin = replace(min, "mm");
    private final String nullSec = replace(sec, "ss");

    private transient final SimpleDateFormat allFormat = new SimpleDateFormat(hour+" "+min+" "+sec);
    private transient final SimpleDateFormat hoursMinuteFormat = new SimpleDateFormat(hour+" "+min);
    private transient final SimpleDateFormat minuteSecondFormat = new SimpleDateFormat(min+" "+sec);
    private transient final SimpleDateFormat hourSecondFormat = new SimpleDateFormat(hour+" "+sec);
    private transient final SimpleDateFormat hourFormat = new SimpleDateFormat(hour);
    private transient final SimpleDateFormat minuteFormat = new SimpleDateFormat(min);
    private transient final SimpleDateFormat secondFormat = new SimpleDateFormat(sec);

    public TimeFormat(long time) {
        this.time = time;
    }

    public SimpleDateFormat toFormat() {
        String format = allFormat.format(time);
        boolean isNullHour = format.contains(nullHour);
        boolean isNullMinute = format.contains(nullMin);
        boolean isNullSecond = format.contains(nullSec);
        if(isNullMinute && isNullSecond) {
            return hourFormat;
        }
        if(isNullMinute && !isNullHour) {
            return hourSecondFormat;
        }
        if(isNullHour && isNullSecond) {
            return minuteFormat;
        }
        if(isNullSecond) {
            return hoursMinuteFormat;
        }
        if(isNullHour && isNullMinute) {
            return secondFormat;
        }
        if(isNullHour) {
            return minuteSecondFormat;
        }
        return allFormat;
    }
    public String toString() {
        return toFormat().format(time);
    }

    private String replace(String target, String replace) {
        return target.replace(replace, "00");
    }

}
