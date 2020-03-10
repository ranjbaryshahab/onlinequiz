package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentDAO extends PagingAndSortingRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Page<Student> findAllByAccount_AccountStatus(AccountStatus accountStatus, Pageable pageable);
}
