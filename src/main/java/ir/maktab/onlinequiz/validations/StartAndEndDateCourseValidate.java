package ir.maktab.onlinequiz.validations;

import java.time.LocalDate;

public class StartAndEndDateCourseValidate {
    public static boolean validate(LocalDate start, LocalDate end) {
        return start.isBefore(end);
    }
}
