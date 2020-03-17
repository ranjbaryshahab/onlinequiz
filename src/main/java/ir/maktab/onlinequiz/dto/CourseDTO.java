package ir.maktab.onlinequiz.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseDTO {
    private String courseId;
    private String courseName;
    private String courseStartDate;
    private String courseEndDate;
}
