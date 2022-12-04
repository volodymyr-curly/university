package ua.foxminded.university.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "durations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Duration {

    @Id
    @Column(name = "duration_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_time", nullable = false, unique = true)
    private String startTime;

    @Column(name = "end_time", nullable = false, unique = true)
    private String endTime;

    @OneToMany(mappedBy = "duration")
    @ToString.Exclude
    private List<Lecture> lectures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration = (Duration) o;
        return Objects.equals(startTime, duration.startTime) && Objects.equals(endTime, duration.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @PreRemove
    private void deleteDuration() {
        lectures.forEach(lecture -> lecture.setDuration(null));
    }
}
