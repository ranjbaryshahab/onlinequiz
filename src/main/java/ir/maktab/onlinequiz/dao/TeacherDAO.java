package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.models.Teacher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeacherDAO extends PagingAndSortingRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
}
