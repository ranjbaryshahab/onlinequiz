package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Integer time;

    @ManyToOne
    @JsonIgnoreProperties({"exam"})
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"question","exam"})
    @ToString.Exclude
    @JoinTable(
            name = "exams_questions",
            joinColumns = @JoinColumn(
                    name = "exam_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "question_id", referencedColumnName = "id"))
    private List<Question> questions = new ArrayList<>();
}
