package ir.maktab.onlinequiz.outcome;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentScoreOutcome {
    private Float multipleChoicesQuestionScore;
    private Float descriptiveQuestionScore;
    private Float finalScore;
}
