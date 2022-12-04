package ua.foxminded.university.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class Teacher {

    @Id
    @Column(name = "teacher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", unique = true)
    @ToString.Exclude
    private Employee employee;

    @Column(name = "degree", nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @ManyToMany(mappedBy = "teachers")
    @ToString.Exclude
    private List<Subject> subjects;

    @OneToMany(mappedBy = "teacher")
    @ToString.Exclude
    private List<Lecture> lectures;

    public void merge(Teacher teacher) {
        subjects.forEach(subject -> subject.getTeachers().remove(this));
        subjects.removeAll(subjects);
        teacher.getSubjects().forEach(subject -> subject.addTeacher(teacher));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(employee, teacher.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee);
    }

    @PreRemove
    private void removeTeacher() {
        lectures.forEach(lecture -> lecture.setTeacher(null));
        subjects.forEach(subject -> subject.getTeachers().remove(this));
    }
}
