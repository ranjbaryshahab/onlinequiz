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
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String topic;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "lessons")
    private Set<Course> courses;
}
