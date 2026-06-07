package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;

public record DisplayEnrollmentResponseDto(
    Long id,
    DisplayCourseResponseDto course,
    String username
) {
    public static DisplayEnrollmentResponseDto from(Enrollment enrollment) {
        return new DisplayEnrollmentResponseDto(
            enrollment.getId(),
            DisplayCourseResponseDto.from(enrollment.getCourse()),
            enrollment.getUser().getUsername()
        );
    }

    public static List<DisplayEnrollmentResponseDto> from(List<Enrollment> enrollments) {
        return enrollments
            .stream()
            .map(DisplayEnrollmentResponseDto::from)
            .toList();
    }
}
