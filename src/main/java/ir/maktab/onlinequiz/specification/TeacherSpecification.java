package ir.maktab.onlinequiz.specification;

import ir.maktab.onlinequiz.dto.TeacherDTO;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.models.Lesson;
import ir.maktab.onlinequiz.models.Person;
import ir.maktab.onlinequiz.models.Teacher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TeacherSpecification implements Specification<Teacher> {
    @Getter
    @Setter
    private TeacherDTO teacherDTO;

    @Override
    public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        setAttributeForCriteria(predicates, root, criteriaBuilder);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    protected void setAttributeForCriteria(List<Predicate> predicates, Root<Teacher> root, CriteriaBuilder criteriaBuilder) {
        setFirstName(root, criteriaBuilder, predicates, teacherDTO.getFirstName());
        setLastName(root, criteriaBuilder, predicates, teacherDTO.getLastName());
        setDegreeOfEducation(root, criteriaBuilder, predicates, teacherDTO.getDegreeOfEducation());
        setTeacherCode(root, criteriaBuilder, predicates, teacherDTO.getTeacherCode());
    }

    private void setFirstName(Root<Teacher> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName.trim() + "%"));
        }
    }

    private void setLastName(Root<Teacher> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName.trim() + "%"));
        }
    }

    private void setTeacherCode(Root<Teacher> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String teacherCode) {
        if (teacherCode != null && !teacherCode.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("teacherCode"), "%" + teacherCode.trim() + "%"));
        }
    }


    private void setDegreeOfEducation(Root<Teacher> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String degreeOfEducation) {
        if (degreeOfEducation != null && !degreeOfEducation.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("degreeOfEducation"), degreeOfEducation));
        }
    }


}
