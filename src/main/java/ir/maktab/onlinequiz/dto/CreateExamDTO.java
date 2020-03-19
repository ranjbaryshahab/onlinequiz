package ir.maktab.onlinequiz.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateExamDTO {
    private String examId;
    private String courseId;
    private String title;
    private Integer time;
    private String description;
}
