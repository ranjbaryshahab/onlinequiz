package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.AddTeacherToCourseDTO;
import ir.maktab.onlinequiz.dto.CourseDTO;
import ir.maktab.onlinequiz.dto.CreateExamDTO;
import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.models.Course;
import ir.maktab.onlinequiz.outcome.CourseDrawOutcome;
import ir.maktab.onlinequiz.services.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class CourseController {
    final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/manager/course/create")
    private ResponseEntity createCourse(@RequestBody CourseDTO courseDTO) {
        if (courseService.createCourse(courseDTO) == null)
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("تاریخ شروع و پایان دوره مطابقت ندارند !");
        return ResponseEntity.ok("دوره با موفقیت اضافه شد");
    }

    @PostMapping("/manager/course/list/{pageNo}/{pageSize}")
    private Page<Course> courses(@PathVariable int pageNo, @PathVariable int pageSize) {
        return courseService.courses(PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/manager/course/list/all/")
    private CourseDrawOutcome courseList() {
        return courseService.courseDraw();
    }

    @PostMapping("/manager/course/delete-all-selected")
    private void deleteAllSelected(@RequestBody IdsListDTO idsListDto) {
        courseService.deleteAllSelected(idsListDto.getListId()
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    @PostMapping("/manager/course/delete-all")
    private void deleteAll() {
        courseService.deleteAll();
    }

    @PostMapping("/manager/course/edit")
    private ResponseEntity editCourse(@RequestBody CourseDTO courseDTO) {
        Course course = courseService.editCourse(courseDTO);
        if (course == null)
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("تاریخ شروع و پایان دوره مطابقت ندارند !");
        return ResponseEntity.ok("با موفقیت ویرایش شد" + course.getCourseName() + "دوره ی ");
    }

    @PostMapping("/manager/course/add-lessons")
    private ResponseEntity addLessonsToCourse(@RequestBody IdsListDTO idsListDTO) {
        Course course = courseService.addLessonsToCourse(idsListDTO);
        if (course == null)
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("تاریخ شروع و پایان دوره مطابقت ندارند !");
        return ResponseEntity.ok("با موفقیت ویرایش شد" + course.getCourseName() + "دوره ی ");
    }


    @PostMapping("/manager/course/lessons/delete-all-selected")
    private void deleteAllSelectedLessonsOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteAllSelectedLessonsOfCourse(idsListDTO);
    }

    @PostMapping("/manager/course/lessons/delete-all")
    private void deleteAllLessonsOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteAllLessonsOfCourse(idsListDTO);
    }

    @PostMapping("/manager/course/teacher/delete")
    private void deleteTeacherOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteTeacherOfCourse(idsListDTO);
    }

    @PostMapping("/manager/course/add-teacher")
    private void addTeacherToCourse(@RequestBody AddTeacherToCourseDTO addTeacherToCourseDTO) {
        courseService.addTeacherToCourse(addTeacherToCourseDTO);
    }

    @PostMapping("/manager/course/add-students")
    private Course addStudentsToCourse(@RequestBody IdsListDTO idsListDTO) {
        return courseService.addStudentsToCourse(idsListDTO);
    }

    @PostMapping("/manager/course/students/delete-all-selected")
    private void deleteAllSelectedStudentsOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteAllSelectedStudentsOfCourse(idsListDTO);
    }

    @PostMapping("/manager/course/students/delete-all")
    private void deleteAllStudentsOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteAllStudentsOfCourse(idsListDTO);
    }

    @PostMapping("/manager/course/search/{pageNo}/{pageSize}")
    private Page<Course> search(@RequestBody CourseDTO courseDTO, @PathVariable int pageNo, @PathVariable int pageSize) {
        return courseService.courseSearch(courseDTO, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/teacher/teacher-course/{username}/{pageNo}/{pageSize}")
    private Page<Course> teacherCourse(@PathVariable String username, @PathVariable int pageNo, @PathVariable int pageSize) {
        return courseService.findAllByTeacher_Account_Username(username, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/teacher/teacher-course/add-exam-to-course")
    private Course addExamToCourse(@RequestBody CreateExamDTO createExamDTO) {
        return courseService.addExamToCourse(createExamDTO);
    }

    @PostMapping("/teacher/teacher-course/exams/delete-all-selected")
    private void deleteAllSelectedExamsOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteAllSelectedExamsOfCourse(idsListDTO);
    }

    @PostMapping("/teacher/teacher-course/exams/delete-all")
    private void deleteAllExamsOfCourse(@RequestBody IdsListDTO idsListDTO) {
        courseService.deleteAllExamsOfCourse(idsListDTO);
    }

    @PostMapping("/student/student-course/{username}/{pageNo}/{pageSize}")
    private Page<Course> studentCourse(@PathVariable String username, @PathVariable int pageNo, @PathVariable int pageSize) {
        return courseService.findByStudents_And_Account_Username(username, PageRequest.of(pageNo, pageSize));
    }
}
