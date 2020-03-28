package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.AddTeacherToCourseDTO;
import ir.maktab.onlinequiz.dto.CourseDTO;
import ir.maktab.onlinequiz.dto.CreateExamDTO;
import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.models.Course;
import ir.maktab.onlinequiz.outcome.CourseDrawOutcome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface CourseService {
    @Secured("ROLE_MANAGER")
    Course createCourse(CourseDTO courseDTO);

    @Secured("ROLE_MANAGER")
    Page<Course> courseSearch(CourseDTO courseDTO, Pageable pageable);

    @Secured("ROLE_MANAGER")
    Page<Course> courses(Pageable pageable);

    @Secured("ROLE_MANAGER")
    CourseDrawOutcome courseDraw();

    @Secured("ROLE_MANAGER")
    void deleteAllSelected(List<Long> idList);

    @Secured("ROLE_MANAGER")
    void deleteAll();

    @Secured("ROLE_MANAGER")
    Course editCourse(CourseDTO courseDTO);

    @Secured("ROLE_MANAGER")
    Course addLessonsToCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void deleteAllSelectedLessonsOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void deleteAllLessonsOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void addTeacherToCourse(AddTeacherToCourseDTO addTeacherToCourseDTO);

    @Secured("ROLE_MANAGER")
    void deleteTeacherOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    Course addStudentsToCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void deleteAllSelectedStudentsOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_MANAGER")
    void deleteAllStudentsOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_TEACHER")
    Page<Course> findAllByTeacher_Account_Username(String username, Pageable pageable);

    @Secured("ROLE_TEACHER")
    Course addExamToCourse(CreateExamDTO createExamDTO);

    @Secured("ROLE_TEACHER")
    void deleteAllSelectedExamsOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_TEACHER")
    void deleteAllExamsOfCourse(IdsListDTO idsListDTO);

    @Secured("ROLE_STUDENT")
    Page<Course> findByStudents_And_Account_Username(String username, Pageable pageable);
}
