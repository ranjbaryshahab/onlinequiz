package ir.maktab.onlinequiz.outcome;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StudentSubmittedExamOutcome {
    Long studentId;
    String studentFirstName;
    String studentLastName;
    String studentCode;
}

