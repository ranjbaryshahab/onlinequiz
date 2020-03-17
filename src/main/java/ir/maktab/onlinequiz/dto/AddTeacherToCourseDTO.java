package ir.maktab.onlinequiz.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTeacherToCourseDTO {
    private String courseId;
    private String teacherId;
}
