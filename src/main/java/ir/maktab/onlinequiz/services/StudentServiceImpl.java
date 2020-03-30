package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.StudentDAO;
import ir.maktab.onlinequiz.dto.StudentDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Student;
import ir.maktab.onlinequiz.specification.StudentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    final StudentDAO studentDAO;
    final StudentSpecification studentSpecification;

    public StudentServiceImpl(StudentDAO studentDAO, StudentSpecification studentSpecification) {
        this.studentDAO = studentDAO;
        this.studentSpecification = studentSpecification;
    }

    @Override
    public Page<Student> paginatedStudent(AccountStatus accountStatus, Pageable pageable) {
        return studentDAO.findAllByAccount_AccountStatus(accountStatus,pageable);
    }

    @Override
    public Page<Student> studentSearch(StudentDTO studentDTO, Pageable pageable) {
        studentSpecification.setStudentDTO(studentDTO);
        return studentDAO.findAll(studentSpecification, pageable);
    }
}
