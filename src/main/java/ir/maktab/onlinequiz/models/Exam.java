package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
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

    private LocalTime time;

    private boolean isEnded;

    private boolean isStarted;

    @ManyToOne
    @JsonIgnoreProperties({"exam"})
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"question", "exam"})
    @ToString.Exclude
    @JoinTable(
            name = "exams_questions",
            joinColumns = @JoinColumn(
                    name = "exam_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "question_id", referencedColumnName = "id"))
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "exam")
    @JsonIgnoreProperties({"scoreList"})
    private List<StudentAnswer> studentAnswers;
}
