package mk.ukim.finki.onlinecoursesbackend.service.domain;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;

public interface EnrollmentService {
    List<Enrollment> findAll();

    List<Enrollment> findAllByUser(User user);

    Boolean existsByCourseAndUser(Course course, User user);

    Integer getAvailableSpotsByCourse(Course course);

    Enrollment enrollByCourseAndUser(Course course, User user);

    Enrollment unenrollByCourseAndUser(Course course, User user);
}
