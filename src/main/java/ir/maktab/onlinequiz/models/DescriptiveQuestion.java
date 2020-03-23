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
@DiscriminatorValue("Descriptive_Question")
public class DescriptiveQuestion extends Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public DescriptiveQuestion(Long id, String text, String title, String answer, List<Score> scoreList, Teacher teacherCreator,List<Exam> exams) {
        super(id, text, title, answer, scoreList, teacherCreator,exams);
    }
}
