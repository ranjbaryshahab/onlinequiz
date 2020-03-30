package ir.maktab.onlinequiz.outcome;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentAnswerListOutcome {
    List<StudentAnswerOutcome> studentAnswerOutcomeList;
}
