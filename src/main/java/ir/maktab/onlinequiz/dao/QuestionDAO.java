package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.Question;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionDAO extends PagingAndSortingRepository<Question, Long>, JpaSpecificationExecutor<Question> {

}
