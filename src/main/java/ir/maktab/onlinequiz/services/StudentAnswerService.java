package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.ScoresExamDTO;
import ir.maktab.onlinequiz.outcome.StudentAnswerListOutcome;
import ir.maktab.onlinequiz.outcome.StudentScoreOutcome;
import ir.maktab.onlinequiz.outcome.StudentSubmittedExamListOutcome;
import org.springframework.security.access.annotation.Secured;

public interface StudentAnswerService {
    @Secured("ROLE_TEACHER")
    StudentSubmittedExamListOutcome getStudentSubmittedExam(Long examId);

    @Secured("ROLE_TEACHER")
    StudentAnswerListOutcome getStudentAnswer(Long examId, Long studentId);

    @Secured("ROLE_TEACHER")
    Boolean setScoresStudent(Long examId, ScoresExamDTO scoresExamDTO);

    @Secured({"ROLE_TEACHER","ROLE_STUDENT"})
    StudentScoreOutcome getScore(Long examId, Long studentId);
}
