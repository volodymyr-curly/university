package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.LectureRoom;

import java.util.Optional;

@Repository
public interface LectureRoomRepository extends JpaRepository<LectureRoom, Integer> {

    Optional<LectureRoom> findByNumber(Integer number);
}
