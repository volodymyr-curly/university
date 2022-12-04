package ua.foxminded.university.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "group_name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    @OneToMany(mappedBy = "group")
    @ToString.Exclude
    private List<Student> students;

    @ManyToMany(mappedBy = "groups")
    @ToString.Exclude
    private List<Subject> subjects;

    @ManyToMany(mappedBy = "groups")
    @ToString.Exclude
    private List<Lecture> lectures;

    public void merge(Group group) {
        subjects.forEach(subject -> subject.getGroups().remove(this));
        subjects.removeAll(subjects);
        group.getSubjects().forEach(subject -> subject.addGroup(group));

        lectures.forEach(lecture -> lecture.getGroups().remove(this));
        lectures.removeAll(lectures);
        group.getLectures().forEach(lecture -> lecture.addGroup(group));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @PreRemove
    private void removeGroup(){
        students.forEach(student -> student.setGroup(null));
        subjects.forEach(subject -> subject.getGroups().remove(this));
        lectures.forEach(lecture -> lecture.getGroups().remove(this));
    }
}
