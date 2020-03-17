package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CourseDAO extends PagingAndSortingRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Page<Course> findAll(Pageable pageable);

    List<Course> findAll();
}
