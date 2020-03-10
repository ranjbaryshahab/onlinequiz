package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeacherDAO extends PagingAndSortingRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    Page<Teacher> findAllByAccount_AccountStatus(AccountStatus accountStatus, Pageable pageable);
}
