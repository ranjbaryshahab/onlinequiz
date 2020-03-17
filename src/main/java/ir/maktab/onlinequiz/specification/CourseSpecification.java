package ir.maktab.onlinequiz.specification;

import ir.maktab.onlinequiz.dto.CourseDTO;
import ir.maktab.onlinequiz.models.Course;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseSpecification implements Specification<Course> {
    @Getter
    @Setter
    private CourseDTO courseDTO;

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        setAttributeForCriteria(predicates, root, criteriaBuilder);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    protected void setAttributeForCriteria(List<Predicate> predicates, Root<Course> root, CriteriaBuilder criteriaBuilder) {
        setCourseName(root, criteriaBuilder, predicates, courseDTO.getCourseName());
        setStartDateCourse(root, criteriaBuilder, predicates, courseDTO.getCourseStartDate());
        setEndDateCourse(root, criteriaBuilder, predicates, courseDTO.getCourseEndDate());
    }

    private void setCourseName(Root<Course> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String courseName) {
        if (courseName != null && !courseName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("courseName"), "%" + courseName.trim() + "%"));
        }
    }

    private void setStartDateCourse(Root<Course> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String startCourse) {
        if (startCourse != null && !startCourse.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("startCourse"), LocalDate.parse(startCourse)));
        }
    }

    private void setEndDateCourse(Root<Course> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String endCourse) {
        if (endCourse != null && !endCourse.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("endCourse"), LocalDate.parse(endCourse)));
        }
    }

}
