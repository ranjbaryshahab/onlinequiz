package ir.maktab.onlinequiz.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teacherCode;

    private Boolean activeCourse;

    @OneToMany(mappedBy = "teacher")
    private Set<Course> courses;
}
