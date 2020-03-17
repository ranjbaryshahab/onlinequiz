package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "teacher")
    private Set<Course> courses;
}
