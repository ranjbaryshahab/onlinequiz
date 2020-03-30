package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.StudentAnswer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerDAO extends PagingAndSortingRepository<StudentAnswer, Long>, JpaSpecificationExecutor<StudentAnswer> {
    Optional<List<StudentAnswer>> findAllByExam_Id(Long exam_id);

    Optional<List<StudentAnswer>> findAllByExam_IdAndStudent_Id(Long exam_id, Long student_id);
}
