package ir.maktab.onlinequiz.outcome;

import lombok.Value;

@Value
public class CourseDrawOutcome {
    Long allCourse;
    Long onPerforming;
    Long notStarted;
    Long ended;
}
