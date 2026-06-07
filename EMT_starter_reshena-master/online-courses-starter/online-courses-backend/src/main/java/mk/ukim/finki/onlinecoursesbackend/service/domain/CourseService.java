package mk.ukim.finki.onlinecoursesbackend.service.domain;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;

public interface CourseService {
    List<Course> findAll();

    List<Course> findAllByAvailability(Boolean available);

    Optional<Course> findById(Long id);

    Optional<Course> findWithLockById(Long id);

    Course create(Course course);

    Optional<Course> update(Long id, Course course);

    Optional<Course> deleteById(Long id);
}
