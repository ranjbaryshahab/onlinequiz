package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.MultipleChoiceQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MultipleChoiceQuestionDAO extends PagingAndSortingRepository<MultipleChoiceQuestion, Long>, JpaSpecificationExecutor<MultipleChoiceQuestion> {
    Page<MultipleChoiceQuestion> findAllByExam_Id(Long examId, Pageable pageable);

    List<MultipleChoiceQuestion> findAllByExam_Id(Long examId);

    Page<MultipleChoiceQuestion> findAllByTeacherCreator_Account_Username(String username, Pageable pageable);
}
