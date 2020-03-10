package ir.maktab.onlinequiz.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LessonDTO {
    private String id;
    private String lessonName;
    private String lessonTopic;
}
