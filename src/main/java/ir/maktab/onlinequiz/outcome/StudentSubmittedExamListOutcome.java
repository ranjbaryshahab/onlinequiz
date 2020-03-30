package ir.maktab.onlinequiz.outcome;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentSubmittedExamListOutcome {
    Set<StudentSubmittedExamOutcome> studentSubmittedExamOutcomeList;
}
