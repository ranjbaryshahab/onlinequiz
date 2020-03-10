package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.StudentDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Student;
import ir.maktab.onlinequiz.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class StudentController {
    final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/manager/student/list/{pageNo}/{pageSize}")
    private Page<Student> getPaginatedStudents(@PathVariable int pageNo, @PathVariable int pageSize) {
        return studentService.paginatedStudent(AccountStatus.ACTIVATE, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/manager/student/search/{pageNo}/{pageSize}")
    private Page<Student> search(@RequestBody StudentDTO studentDTO, @PathVariable int pageNo, @PathVariable int pageSize) {
        return studentService.studentSearch(studentDTO, PageRequest.of(pageNo, pageSize));
    }
}
