package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;

public record DisplayEnrollmentDetailsResponseDto(
    Long id,
    DisplayCourseDetailsResponseDto course,
    String username
) {
    public static DisplayEnrollmentDetailsResponseDto from(Enrollment enrollment) {
        return new DisplayEnrollmentDetailsResponseDto(
            enrollment.getId(),
            DisplayCourseDetailsResponseDto.from(enrollment.getCourse()),
            enrollment.getUser().getUsername()
        );
    }

    public static List<DisplayEnrollmentDetailsResponseDto> from(List<Enrollment> enrollments) {
        return enrollments
            .stream()
            .map(DisplayEnrollmentDetailsResponseDto::from)
            .toList();
    }
}
