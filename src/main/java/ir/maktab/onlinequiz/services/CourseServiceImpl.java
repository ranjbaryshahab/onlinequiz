package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.*;
import ir.maktab.onlinequiz.dto.AddTeacherToCourseDTO;
import ir.maktab.onlinequiz.dto.CourseDTO;
import ir.maktab.onlinequiz.dto.CreateExamDTO;
import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.models.*;
import ir.maktab.onlinequiz.outcome.CourseDrawOutcome;
import ir.maktab.onlinequiz.specification.CourseSpecification;
import ir.maktab.onlinequiz.utils.MyTime;
import ir.maktab.onlinequiz.validations.StartAndEndDateCourseValidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CourseServiceImpl implements CourseService {
    final CourseDAO courseDAO;
    final LessonDAO lessonDAO;
    final StudentDAO studentDAO;
    final TeacherDAO teacherDAO;
    final CourseSpecification courseSpecification;
    final ExamDAO examDAO;

    public CourseServiceImpl(CourseDAO courseDAO, LessonDAO lessonDAO, StudentDAO studentDAO, TeacherDAO teacherDAO, CourseSpecification courseSpecification, ExamDAO examDAO) {
        this.courseDAO = courseDAO;
        this.lessonDAO = lessonDAO;
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.courseSpecification = courseSpecification;
        this.examDAO = examDAO;
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
                    endDate,
                    null
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

        if (StartAndEndDateCourseValidate.validate(startDate, endDate)) {
            Optional<Course> courseOptional = courseDAO.findById(Long.parseLong(courseDTO.getCourseId()));
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
                return courseDAO.save(new Course(
                        course.getId(),
                        courseDTO.getCourseName(),
                        course.getLessons(),
                        course.getTeacher(),
                        course.getStudents(),
                        startDate,
                        endDate,
                        course.getExams()
                ));
            }
        }
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

    @Override
    public Page<Course> findAllByTeacher_Account_Username(String username, Pageable pageable) {
        return courseDAO.findAllByTeacher_Account_Username(username, pageable);
    }

    @Override
    public Course addExamToCourse(CreateExamDTO createExamDTO) {
        Optional<Course> courseOptional = courseDAO.findById(Long.parseLong(createExamDTO.getCourseId()));
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            Exam exam = new Exam(
                    null,
                    createExamDTO.getTitle(),
                    createExamDTO.getDescription(),
                    MyTime.convertStringToTime(createExamDTO.getTime()),
                    false,
                    false,
                    course,
                    null,
                    null
            );
            List<Exam> exams = new ArrayList<>();
            exams.add(exam);
            if (course.getExams() == null) {
                course.setExams(exams);
            } else {
                course.getExams().add(exam);
            }
            return courseDAO.save(new Course(
                    course.getId(),
                    course.getCourseName(),
                    course.getLessons(),
                    course.getTeacher(),
                    course.getStudents(),
                    course.getStartCourse(),
                    course.getEndCourse(),
                    course.getExams()
            ));
        }
        return null;
    }

    @Override
    public void deleteAllSelectedExamsOfCourse(IdsListDTO idsListDTO) {
        Optional<Course> courseOptional = courseDAO.findById(Long.parseLong(idsListDTO.getSecret()));
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            if (course.getExams() != null && !course.getExams().isEmpty()) {
                List<Long> ids = idsListDTO.getListId()
                        .stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                course.getExams().stream().filter(exam -> ids.contains(exam.getId())).collect(Collectors.toList()).
                        forEach(exam -> exam.setCourse(null));
                course.getExams().removeIf(exam -> ids.contains(exam.getId()));

            }
            courseDAO.save(course);
        }
    }

    @Override
    public void deleteAllExamsOfCourse(IdsListDTO idsListDTO) {
        Optional<Course> courseOptional = courseDAO.findById(Long.parseLong(idsListDTO.getSecret()));
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            if (course.getExams() != null && !course.getExams().isEmpty()) {
                course.getExams().forEach(exam -> exam.setCourse(null));
                course.getExams().removeAll(course.getExams());
            }
            courseDAO.save(course);
        }
    }

    @Override
    public Page<Course> findByStudents_And_Account_Username(String username, Pageable pageable) {
        Optional<Student> studentOptional = studentDAO.findByAccount_Username(username);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            List<Student> students = new ArrayList<>();
            students.add(student);
            return courseDAO.findAllByStudentsIn(students, pageable);
        }
        return null;
    }


}

