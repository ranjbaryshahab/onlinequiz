package ir.maktab.onlinequiz.specification;

import ir.maktab.onlinequiz.dto.LessonDTO;
import ir.maktab.onlinequiz.models.Lesson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LessonSpecification implements Specification<Lesson> {
    @Getter
    @Setter
    private LessonDTO lessonDto;

    @Override
    public Predicate toPredicate(Root<Lesson> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        setAttributeForCriteria(predicates, root, criteriaBuilder);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    protected void setAttributeForCriteria(List<Predicate> predicates, Root<Lesson> root, CriteriaBuilder criteriaBuilder) {
        setLessonName(root, criteriaBuilder, predicates, lessonDto.getLessonName());
        setFirstName(root, criteriaBuilder, predicates, lessonDto.getLessonTopic());
    }

    private void setLessonName(Root<Lesson> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String lessonName) {
        if (lessonName != null && !lessonName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + lessonName.trim() + "%"));
        }
    }

    private void setFirstName(Root<Lesson> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String topic) {
        if (topic != null && !topic.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("topic"), "%" + topic.trim() + "%"));
        }
    }
}
