package mk.ukim.finki.onlinecoursesbackend.service.application;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;

public interface EnrollmentApplicationService {
    List<DisplayEnrollmentDetailsResponseDto> findAllByUserWithDetails(User user);

    Boolean existsByCourseAndUser(Long courseId, User user);

    Integer getAvailableSpotsByCourse(Long courseId);

    DisplayEnrollmentResponseDto enrollByCourseAndUser(Long courseId, User user);

    DisplayEnrollmentResponseDto unenrollByCourseAndUser(Long courseId, User user);
}
