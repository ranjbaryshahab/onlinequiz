package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.TeacherDAO;
import ir.maktab.onlinequiz.dto.TeacherDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Teacher;
import ir.maktab.onlinequiz.specification.TeacherSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    final TeacherDAO teacherDAO;
    final TeacherSpecification teacherSpecification;

    public TeacherServiceImpl(TeacherDAO teacherDAO, TeacherSpecification teacherSpecification) {
        this.teacherDAO = teacherDAO;
        this.teacherSpecification = teacherSpecification;
    }

    @Override
    public Page<Teacher> paginatedTeacher(AccountStatus accountStatus, Pageable pageable) {
        return teacherDAO.findAllByAccount_AccountStatus(accountStatus, pageable);
    }

    @Override
    public Page<Teacher> teacherSearch(TeacherDTO teacherDTO, Pageable pageable) {
        teacherSpecification.setTeacherDTO(teacherDTO);
        return teacherDAO.findAll(teacherSpecification, pageable);
    }
}
