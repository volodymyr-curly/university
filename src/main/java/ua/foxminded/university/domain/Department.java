package ua.foxminded.university.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Department {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "department_name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Group> groups;

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Employee> employees;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @PreRemove
    private void removeDepartment() {
        employees.forEach(employee -> employee.setDepartment(null));
        groups.forEach(group -> group.setDepartment(null));
    }
}
