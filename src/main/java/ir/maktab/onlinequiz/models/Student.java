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
@DiscriminatorValue("STUDENT")
public class Student extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentCode;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses;
}
