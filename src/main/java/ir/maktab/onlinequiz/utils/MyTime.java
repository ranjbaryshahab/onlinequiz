package ir.maktab.onlinequiz.utils;

import java.time.LocalTime;

public class MyTime {
    public static LocalTime convertStringToTime(String time) {
        if (time.length() > 6) {
            return LocalTime.parse(time);
        } else {
            int timer = Integer.parseInt(time);
            int hour = timer / 60;
            int minute = timer % 60;
            return LocalTime.of(hour, minute, 0, 0);
        }
    }
}
