package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.Course;
import ir.maktab.onlinequiz.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseDAO extends PagingAndSortingRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Page<Course> findAll(Pageable pageable);

    List<Course> findAll();

    Page<Course> findAllByTeacher_Account_Username(String username, Pageable pageable);

    Page<Course> findAllByStudentsIn(List<Student> students, Pageable pageable);
}
