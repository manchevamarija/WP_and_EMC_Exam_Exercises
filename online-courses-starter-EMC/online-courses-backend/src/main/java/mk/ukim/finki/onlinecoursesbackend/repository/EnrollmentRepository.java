package mk.ukim.finki.onlinecoursesbackend.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @EntityGraph(attributePaths = {"course", "course.topic", "user"})
    @Query("select e from Enrollment e")
    List<Enrollment> findAllByUser(User user);

    Optional<Enrollment> findByCourseAndUser(Course course, User user);

    Integer countByCourse(Course course);

}
