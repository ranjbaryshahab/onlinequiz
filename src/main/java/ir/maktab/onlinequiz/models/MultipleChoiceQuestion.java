package ir.maktab.onlinequiz.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@DiscriminatorValue("Multiple_Choice_Question")
public class MultipleChoiceQuestion extends Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> options;

    public MultipleChoiceQuestion(Long id, String text, String title, String answer, List<Score> scoreList, Teacher teacherCreator, List<Exam> exams, List<String> options) {
        super(id, text, title, answer, scoreList, teacherCreator, exams);
        this.options = options;
    }
}
