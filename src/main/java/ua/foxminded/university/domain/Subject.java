package ua.foxminded.university.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Subject {

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "subject_name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "groups_subjects",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @ToString.Exclude
    private List<Group> groups;

    @ManyToMany
    @JoinTable(
        name = "teachers_subjects",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @ToString.Exclude
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "subject")
    @ToString.Exclude
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "subject")
    @ToString.Exclude
    private List<Mark> marks;

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(name, subject.name) && Objects.equals(startDate, subject.startDate) && Objects.equals(endDate, subject.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate);
    }

    @PreRemove
    private void removeSubject(){
        groups.forEach(group -> group.getSubjects().remove(this));
        teachers.forEach(teacher -> teacher.getSubjects().remove(this));
        lectures.forEach(lecture -> lecture.setSubject(null));
        marks.forEach(mark -> mark.setSubject(null));
    }
}

