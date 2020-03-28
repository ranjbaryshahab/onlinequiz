package ir.maktab.onlinequiz.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswersExamDTO {
    private List<AddAnswerDTO> addAnswersDTO;
    private String submitStudent;
    private String studentUsername;
}
