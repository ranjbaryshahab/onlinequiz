package ir.maktab.onlinequiz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "PERSON_TYPE", discriminatorType = DiscriminatorType.STRING)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String fathersName;

    private String nationalCode;

    private LocalDate birthOfDate;

    private String degreeOfEducation;

    @JsonIgnoreProperties({"person","roles"})
    @OneToOne(mappedBy = "person")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    private Communication communication;
}
