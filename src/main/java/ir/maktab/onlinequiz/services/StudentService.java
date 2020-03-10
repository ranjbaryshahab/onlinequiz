package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.StudentDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

public interface StudentService {
    @Secured("ROLE_MANAGER")
    Page<Student> paginatedStudent(AccountStatus accountStatus, Pageable pageable);

    @Secured("ROLE_MANAGER")
    Page<Student> studentSearch(StudentDTO studentDTO, Pageable pageable);
}
