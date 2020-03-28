package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String context;

    private boolean isCorrected;

    @ManyToOne
    @JsonIgnoreProperties({"courses"})
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonIgnoreProperties({"questions" , "course", "studentAnswers"})
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnoreProperties({"question", "exam", "studentAnswers"})
    private Question question;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "score_id")
    @JsonIgnoreProperties({"studentAnswers", "question"})
    private Score score;
}
