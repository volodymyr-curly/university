package ua.foxminded.university.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lecture_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LectureRoom {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "room_number", nullable = false, unique = true)
    private Integer number;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "room")
    @ToString.Exclude
    private List<Lecture> lectures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureRoom room = (LectureRoom) o;
        return Objects.equals(number, room.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @PreRemove
    private void removeRoom(){
        lectures.forEach(lecture -> lecture.setRoom(null));
    }
}
