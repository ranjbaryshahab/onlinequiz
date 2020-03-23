package ir.maktab.onlinequiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class QuestionDTO {
    private Long examId;
    private Long questionId;
    private String title;
    private String text;
    private String answer;
    private String type;
    private List<String> options;
    private Float point;
}
