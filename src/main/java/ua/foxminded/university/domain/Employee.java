package ua.foxminded.university.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Employee extends Person {

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    @Column(name = "job_title", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @Column(name = "employment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Employment employmentType;

    @Column(name = "employment_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentDate;

    @OneToOne(mappedBy = "employee")
    private Teacher teacher;

    @PreRemove
    private void removeEmployee() {
        teacher.setEmployee(null);
    }
}


