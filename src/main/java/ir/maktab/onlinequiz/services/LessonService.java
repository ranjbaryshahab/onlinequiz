package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.LessonDTO;
import ir.maktab.onlinequiz.models.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface LessonService {
    @Secured("ROLE_MANAGER")
    Lesson createLesson(LessonDTO lessonDto);

    @Secured("ROLE_MANAGER")
    Page<Lesson> lessonSearch(LessonDTO lessonDto, Pageable pageable);

    @Secured("ROLE_MANAGER")
    Page<Lesson> paginatedLessons(Pageable pageable);

    @Secured("ROLE_MANAGER")
    Lesson editLesson(LessonDTO lessonDto);

    @Secured("ROLE_MANAGER")
    void deleteAllSelected(List<Long> idList);

    @Secured("ROLE_MANAGER")
    void deleteAll();

}
