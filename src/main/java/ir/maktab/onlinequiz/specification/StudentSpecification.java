package ir.maktab.onlinequiz.specification;

import ir.maktab.onlinequiz.dto.StudentDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.models.Student;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentSpecification implements Specification<Student> {
    @Getter
    @Setter
    private StudentDTO studentDTO;

    @Override
    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        setAttributeForCriteria(predicates, root, criteriaBuilder);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    protected void setAttributeForCriteria(List<Predicate> predicates, Root<Student> root, CriteriaBuilder criteriaBuilder) {
        setFirstName(root, criteriaBuilder, predicates, studentDTO.getFirstName());
        setLastName(root, criteriaBuilder, predicates, studentDTO.getLastName());
        setDegreeOfEducation(root, criteriaBuilder, predicates, studentDTO.getDegreeOfEducation());
        setStudentCode(root, criteriaBuilder, predicates, studentDTO.getStudentCode());
    }

    private void setFirstName(Root<Student> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName.trim() + "%"));
        }
    }

    private void setLastName(Root<Student> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName.trim() + "%"));
        }
    }

    private void setStudentCode(Root<Student> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String studentCode) {
        if (studentCode != null && !studentCode.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("studentCode"), "%" + studentCode.trim() + "%"));
        }
    }


    private void setDegreeOfEducation(Root<Student> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String degreeOfEducation) {
        if (degreeOfEducation != null && !degreeOfEducation.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("degreeOfEducation"), degreeOfEducation));
        }
    }

    private void setStatus(Root<Student> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, String status) {
        if (status != null && !status.isEmpty()) {
            Join<Student, Account> teacherAccountJoin = root.join("account");
            predicates.add(criteriaBuilder.equal(root.get("accountStatus"), AccountStatus.valueOf(studentDTO.getStatus())));
        }
    }
}
