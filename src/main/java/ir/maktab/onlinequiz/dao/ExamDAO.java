package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.Exam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ExamDAO extends PagingAndSortingRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {
}
