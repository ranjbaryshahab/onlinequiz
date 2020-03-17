package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.CourseDAO;
import ir.maktab.onlinequiz.dao.LessonDAO;
import ir.maktab.onlinequiz.dao.StudentDAO;
import ir.maktab.onlinequiz.dao.TeacherDAO;
import ir.maktab.onlinequiz.dto.AddTeacherToCourseDTO;
import ir.maktab.onlinequiz.dto.CourseDTO;
import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.models.Course;
import ir.maktab.onlinequiz.models.Lesson;
import ir.maktab.onlinequiz.models.Student;
import ir.maktab.onlinequiz.models.Teacher;
import ir.maktab.onlinequiz.outcome.CourseDrawOutcome;
import ir.maktab.onlinequiz.specification.CourseSpecification;
import ir.maktab.onlinequiz.validations.StartAndEndDateCourseValidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CourseServiceImpl implements CourseService {
    final CourseDAO courseDAO;
    final LessonDAO lessonDAO;
    final StudentDAO studentDAO;
    final TeacherDAO teacherDAO;
    final CourseSpecification courseSpecification;

    public CourseServiceImpl(CourseDAO courseDAO, LessonDAO lessonDAO, StudentDAO studentDAO, TeacherDAO teacherDAO, CourseSpecification courseSpecification) {
        this.courseDAO = courseDAO;
        this.lessonDAO = lessonDAO;
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.courseSpecification = courseSpecification;
    }

    @Override
    public Course createCourse(CourseDTO courseDTO) {
        LocalDate startDate = LocalDate.parse(courseDTO.getCourseStartDate());
        LocalDate endDate = LocalDate.parse(courseDTO.getCourseEndDate());

        if (StartAndEndDateCourseValidate.validate(startDate, endDate))
            return courseDAO.save(new Course(
                    null,
                    courseDTO.getCourseName(),
                    null,
                    null,
                    null,
                    startDate,
                    endDate
            ));
        return null;
    }

    @Override
    public Page<Course> courseSearch(CourseDTO courseDTO, Pageable pageable) {
        courseSpecification.setCourseDTO(courseDTO);
        return courseDAO.findAll(courseSpecification, pageable);
    }

    @Override
    public Page<Course> courses(Pageable pageable) {
        return courseDAO.findAll(pageable);
    }

    @Override
    public CourseDrawOutcome courseDraw() {
        List<Course> courses = courseDAO.findAll();
        long allCourse = courses.size();
        long ended = 0;
        long notStarted = 0;
        long onPerforming = 0;
        for (Course course : courses) {
            if (course.getStartCourse().isBefore(LocalDate.now())) {
                if (course.getEndCourse().isBefore(LocalDate.now())) {
                    ended++;
                } else if (course.getEndCourse().isAfter(LocalDate.now())) {
                    onPerforming++;
                }
            } else if (course.getStartCourse().isAfter(LocalDate.now())) {
                notStarted++;
            }
        }
        return new CourseDrawOutcome(allCourse, onPerforming, notStarted, ended);
    }

    @Override
    public void deleteAllSelected(List<Long> idList) {
        courseDAO.findAllById(idList).forEach(courseDAO::delete);
    }

    @Override
    public void deleteAll() {
        courseDAO.deleteAll();
    }

    @Override
    public Course editCourse(CourseDTO courseDTO) {
        LocalDate startDate = LocalDate.parse(courseDTO.getCourseStartDate());
        LocalDate endDate = LocalDate.parse(courseDTO.getCourseEndDate());

        if (StartAndEndDateCourseValidate.validate(startDate, endDate))
            return courseDAO.save(new Course(
                    Long.parseLong(courseDTO.getCourseId()),
                    courseDTO.getCourseName(),
                    null,
                    null,
                    null,
                    startDate,
                    endDate
            ));
        return null;
    }

    @Override
    public Course addLessonsToCourse(IdsListDTO idsListDTO) {
        if (courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).isPresent()) {
            Course course = courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).get();
            List<Lesson> lessons = new ArrayList<>();
            lessonDAO.findAllById(idsListDTO.getListId()
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList())).forEach(lessons::add);
            if (course.getLessons() == null)
                course.setLessons(lessons);
            else {
                course.setLessons(Stream.of(course.getLessons(), lessons)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
            }

            return courseDAO.save(course);
        }
        return null;
    }

    @Override
    public void deleteAllSelectedLessonsOfCourse(IdsListDTO idsListDTO) {
        if (courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).isPresent()) {
            Course course = courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).get();
            if (course.getLessons() != null && !course.getLessons().isEmpty()) {
                List<Long> ids = idsListDTO.getListId()
                        .stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                course.getLessons().removeIf(lesson -> ids.contains(lesson.getId()));
            }
            courseDAO.save(course);
        }
    }

    @Override
    public void deleteAllLessonsOfCourse(IdsListDTO idsListDTO) {
        if (courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).isPresent()) {
            Course course = courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).get();
            if (course.getLessons() != null && !course.getLessons().isEmpty()) {
                course.getLessons().removeAll(course.getLessons());
            }
            courseDAO.save(course);
        }
    }

    @Override
    public void addTeacherToCourse(AddTeacherToCourseDTO addTeacherToCourseDTO) {
        Optional<Course> courseOptional = courseDAO.findById(Long.parseLong(addTeacherToCourseDTO.getCourseId()));
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            Optional<Teacher> teacherOptional = teacherDAO.findById(Long.parseLong(addTeacherToCourseDTO.getTeacherId()));
            if (teacherOptional.isPresent()) {
                course.setTeacher(teacherOptional.get());
                courseDAO.save(course);
            }
        }
    }

    @Override
    public void deleteTeacherOfCourse(IdsListDTO idsListDTO) {
        if (courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).isPresent()) {
            Course course = courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).get();
            course.setTeacher(null);
            courseDAO.save(course);
        }
    }

    @Override
    public Course addStudentsToCourse(IdsListDTO idsListDTO) {
        Optional<Course> courseOptional = courseDAO.findById(Long.parseLong(idsListDTO.getSecret()));
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            List<Student> students = new ArrayList<>();
            studentDAO.findAllById(idsListDTO.getListId()
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList())).forEach(students::add);
            if (course.getStudents() == null)
                course.setStudents(students);
            else {
                course.setStudents(Stream.of(course.getStudents(), students)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
            }
            courseDAO.save(course);
            return course;
        }
        return null;
    }

    @Override
    public void deleteAllSelectedStudentsOfCourse(IdsListDTO idsListDTO) {
        if (courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).isPresent()) {
            Course course = courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).get();
            if (course.getStudents() != null && !course.getStudents().isEmpty()) {
                List<Long> ids = idsListDTO.getListId()
                        .stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                course.getStudents().removeIf(student -> ids.contains(student.getId()));
            }
            courseDAO.save(course);
        }
    }

    @Override
    public void deleteAllStudentsOfCourse(IdsListDTO idsListDTO) {
        if (courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).isPresent()) {
            Course course = courseDAO.findById(Long.parseLong(idsListDTO.getSecret())).get();
            if (course.getStudents() != null && !course.getStudents().isEmpty()) {
                course.getStudents().removeAll(course.getStudents());
            }
            courseDAO.save(course);
        }
    }
}
