package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Question_TYPE", discriminatorType = DiscriminatorType.STRING)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private String text;

    @Column
    private String title;

    @Lob
    private String answer;

    @ToString.Exclude
    @JsonIgnoreProperties({"question","studentAnswers"})
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Score> scoreList = new ArrayList<>();

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "teacher_id")
    private Teacher teacherCreator;

    @ToString.Exclude
    @JsonIgnoreProperties({"questions", "course"})
    @ManyToMany(mappedBy = "questions")
    private List<Exam> exam;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"question"})
    private List<StudentAnswer> studentAnswers;
}
