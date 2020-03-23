package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.QuestionDTO;
import ir.maktab.onlinequiz.models.DescriptiveQuestion;
import ir.maktab.onlinequiz.models.MultipleChoiceQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

public interface QuestionService {

    @Secured("ROLE_TEACHER")
    Page<DescriptiveQuestion> descriptiveQuestionPaginate(Long examId, Pageable pageable);

    @Secured("ROLE_TEACHER")
    Page<MultipleChoiceQuestion> multipleChoiceQuestionPaginate(Long examId, Pageable pageable);

    @Secured("ROLE_TEACHER")
    Page<MultipleChoiceQuestion> multipleQuestionPaginateTeacherQuestionBank(String username, Pageable pageable);

    @Secured("ROLE_TEACHER")
    Page<DescriptiveQuestion> descriptiveQuestionPaginateTeacherQuestionBank(String username, Pageable pageable);

    @Secured("ROLE_TEACHER")
    void editQuestion(QuestionDTO questionDTO);
}
