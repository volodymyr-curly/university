package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.Duration;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Integer> {

    boolean existsByStartTime(String startTime);

    boolean existsByEndTime(String endTime);
}
