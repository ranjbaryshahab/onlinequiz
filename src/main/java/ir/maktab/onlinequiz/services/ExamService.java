package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.*;
import org.springframework.security.access.annotation.Secured;

public interface ExamService {
    @Secured("ROLE_TEACHER")
    void createQuestion(QuestionDTO questionDTO);

    @Secured("ROLE_TEACHER")
    void addQuestionFromTeacherQuestionBankToExam(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void deleteAllSelectedQuestionOfExam(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void deleteAllQuestionOfExam(Long examId);

    @Secured("ROLE_TEACHER")
    void editExam(CreateExamDTO createExamDTO);

    @Secured("ROLE_TEACHER")
    void startExam(Long id);

    @Secured("ROLE_STUDENT")
    Long getExamTime(Long id);

    @Secured("ROLE_STUDENT")
    void addAnswerByStudent(Long examId, AnswersExamDTO answersExamDTO);

    @Secured("ROLE_TEACHER")
    void endExam(Long id);
}
