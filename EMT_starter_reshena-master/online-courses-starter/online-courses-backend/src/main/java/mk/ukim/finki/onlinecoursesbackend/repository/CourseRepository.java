package mk.ukim.finki.onlinecoursesbackend.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findTopByOrderByIdDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Course c where c.id = :id")
    Optional<Course> findWithLockById(@Param("id") Long id);
}
