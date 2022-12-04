package ua.foxminded.university.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lectures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Lecture {

    @Id
    @Column(name = "lecture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
        name = "groups_lectures",
        joinColumns = @JoinColumn(name = "lecture_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @ToString.Exclude
    private List<Group> groups;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private LectureRoom room;

    @ManyToOne
    @JoinColumn(name = "duration_id", referencedColumnName = "duration_id")
    private Duration duration;

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public void addGroup(Group group) {
        groups.add(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(subject, lecture.subject) && Objects.equals(teacher, lecture.teacher) && Objects.equals(room, lecture.room) && Objects.equals(duration, lecture.duration) && Objects.equals(date, lecture.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, teacher, room, duration, date);
    }
}
