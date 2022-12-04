package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.Mark;
import ua.foxminded.university.domain.MarkValue;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {

    @Query("select case when count(m) > 0 then true else false end from Mark m" +
        " where m.value=?1 and m.student.id=?2 and m.subject.id=?3")
    boolean existsSameMark(MarkValue value, int studentId, int subjectId);

    List<Mark> findByStudentId(int id);

    List<Mark> findBySubjectId(int id);
}
