package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.dto.QuestionDTO;
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
}
