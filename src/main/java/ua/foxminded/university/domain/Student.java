package ua.foxminded.university.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Entity
@Table(name = "students")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Student extends Person {

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @Column(name = "education_form", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private Employment educationForm;

    @Column(name = "enrollment_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private LocalDate enrollmentDate;

    @OneToMany(mappedBy = "student")
    @ToString.Exclude
    private List<Mark> marks;

    @PreRemove
    private void removeStudent(){
        marks.forEach(mark -> mark.setStudent(null));
    }
}
