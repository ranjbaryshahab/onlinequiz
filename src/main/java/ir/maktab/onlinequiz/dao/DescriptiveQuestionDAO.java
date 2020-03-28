package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.DescriptiveQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DescriptiveQuestionDAO extends PagingAndSortingRepository<DescriptiveQuestion, Long>, JpaSpecificationExecutor<DescriptiveQuestion> {
    Page<DescriptiveQuestion> findAllByExam_Id(Long examId, Pageable pageable);

    List<DescriptiveQuestion> findAllByExam_Id(Long examId);

    Page<DescriptiveQuestion> findAllByTeacherCreator_Account_Username(String username, Pageable pageable);
}
