package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.TeacherDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

public interface TeacherService {
    @Secured("ROLE_MANAGER")
    Page<Teacher> paginatedTeacher(AccountStatus accountStatus, Pageable pageable);

    @Secured("ROLE_MANAGER")
    Page<Teacher> teacherSearch(TeacherDTO teacherDTO, Pageable pageable);
}
