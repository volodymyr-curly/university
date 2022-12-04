package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.domain.MarkValue;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    @Query("select case when count(l) > 0 then true else false end from Lecture l" +
        " where l.date=?1 and l.subject.id=?2 and l.teacher.id=?3 and l.room.id=?4 and l.duration.id=?5")
    boolean existsSameLecture(LocalDate date, int subjectId, int teacherId, int roomId, int durationId);

    List<Lecture> findBySubjectId(int id);

    List<Lecture> findByTeacherId(int id);

    List<Lecture> findByRoomId(int id);

    List<Lecture> findByDurationId(int id);

    List<Lecture> findByGroupsId(int id);
}
