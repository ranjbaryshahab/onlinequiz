package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.Lesson;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LessonDAO extends PagingAndSortingRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

}
