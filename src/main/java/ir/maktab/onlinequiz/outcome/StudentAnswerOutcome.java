package ir.maktab.onlinequiz.outcome;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentAnswerOutcome {
    Long answerId;
    Long questionId;
    String questionType;
    String defaultAnswer;
    String studentAnswer;
    Float defaultPoint;
}
