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
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float point;

    @ManyToOne
    @JsonIgnoreProperties({"scoreList","exam","studentAnswers"})
    @JoinColumn(name = "questionId")
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties({"questions","exam","course","studentAnswers"})
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
