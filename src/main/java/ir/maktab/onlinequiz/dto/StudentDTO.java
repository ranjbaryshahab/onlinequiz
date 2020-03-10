package ir.maktab.onlinequiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class StudentDTO {
    private String firstName;
    private String lastName;
    private String degreeOfEducation;
    private String studentCode;
    private String status;
}
