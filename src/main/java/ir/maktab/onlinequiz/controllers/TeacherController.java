package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.TeacherDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Teacher;
import ir.maktab.onlinequiz.services.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class TeacherController {

    final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/manager/teacher/list/{pageNo}/{pageSize}")
    private Page<Teacher> getPaginatedTeachers(@PathVariable int pageNo, @PathVariable int pageSize) {
        return teacherService.paginatedTeacher(AccountStatus.ACTIVATE, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/manager/teacher/search/{pageNo}/{pageSize}")
    private Page<Teacher> search(@RequestBody TeacherDTO teacherDTO, @PathVariable int pageNo, @PathVariable int pageSize) {
        return teacherService.teacherSearch(teacherDTO, PageRequest.of(pageNo, pageSize));
    }
}
