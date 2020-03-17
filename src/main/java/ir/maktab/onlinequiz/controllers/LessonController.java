package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.*;
import ir.maktab.onlinequiz.models.Lesson;
import ir.maktab.onlinequiz.services.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class LessonController {
    final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("/manager/lesson/create")
    private Lesson createLesson(@RequestBody LessonDTO lessonDto) {
        return lessonService.createLesson(lessonDto);
    }

    @PostMapping("/manager/lessons/list/{pageNo}/{pageSize}")
    private Page<Lesson> getPaginatedLessons(@PathVariable int pageNo, @PathVariable int pageSize) {
        return lessonService.paginatedLessons(PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/manager/lesson/edit")
    private Lesson editLesson(@RequestBody LessonDTO lessonDto) {
        return lessonService.editLesson(lessonDto);
    }

    @PostMapping("/manager/lesson/delete-all-selected")
    private void deleteAllSelected(@RequestBody IdsListDTO idsListDto) {
        lessonService.deleteAllSelected(idsListDto.getListId()
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    @PostMapping("/manager/lesson/delete-all")
    private void deleteAll() {
        lessonService.deleteAll();
    }

    @PostMapping("/manager/lesson/search/{pageNo}/{pageSize}")
    private Page<Lesson> search(@RequestBody LessonDTO lessonDto, @PathVariable int pageNo, @PathVariable int pageSize) {
        return lessonService.lessonSearch(lessonDto, PageRequest.of(pageNo, pageSize));
    }
}
