package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    @ManyToMany
    @ToString.Exclude
    @JsonIgnoreProperties({"courses"})
    @JoinTable(
            name = "courses_lessons",
            joinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "lesson_id", referencedColumnName = "id"))
    private List<Lesson> lessons;

    @JsonIgnoreProperties({"courses"})
    @ToString.Exclude
    @ManyToOne
    private Teacher teacher;

    @JsonIgnoreProperties({"courses"})
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "students_courses",
            joinColumns = @JoinColumn(
                    name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"))
    private List<Student> students;

    private LocalDate startCourse;

    private LocalDate endCourse;

    @JsonIgnoreProperties({"course"})
    @ToString.Exclude
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams;
}
