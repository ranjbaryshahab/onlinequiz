package ir.maktab.onlinequiz.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddAnswerDTO {
    private String context;
    private Long questionId;
    private String questionType;
}
