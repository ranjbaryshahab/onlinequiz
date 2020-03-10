package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.LessonDAO;
import ir.maktab.onlinequiz.dto.LessonDto;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.models.Lesson;
import ir.maktab.onlinequiz.specification.LessonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {
    final LessonDAO lessonDAO;
    final LessonSpecification lessonSpecification;

    public LessonServiceImpl(LessonDAO lessonDAO, LessonSpecification lessonSpecification) {
        this.lessonDAO = lessonDAO;
        this.lessonSpecification = lessonSpecification;
    }

    @Override
    public Lesson createLesson(LessonDto lessonDto) {
        return lessonDAO.save(
                new Lesson(null, lessonDto.getLessonName(), lessonDto.getLessonTopic(), null)
        );
    }

    @Override
    public Page<Lesson> lessonSearch(LessonDto lessonDto, Pageable pageable) {
        lessonSpecification.setLessonDto(lessonDto);
        return lessonDAO.findAll(lessonSpecification, pageable);
    }

    @Override
    public Page<Lesson> paginatedLessons(Pageable pageable) {
        return lessonDAO.findAll(pageable);
    }

    @Override
    public Lesson editLesson(LessonDto lessonDto) {
        return lessonDAO.save(
                new Lesson(Long.parseLong(lessonDto.getId()), lessonDto.getLessonName(), lessonDto.getLessonTopic(), null)
        );
    }


    @Override
    public void deleteAllSelected(List<Long> idList) {
        lessonDAO.findAllById(idList).forEach(lessonDAO::delete);
    }

    @Override
    public void deleteAll() {
        lessonDAO.deleteAll();
    }
}
