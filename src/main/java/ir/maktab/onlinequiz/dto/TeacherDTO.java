package ir.maktab.onlinequiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TeacherDTO {
    private String firstName;
    private String lastName;
    private String degreeOfEducation;
    private String teacherCode;
}
