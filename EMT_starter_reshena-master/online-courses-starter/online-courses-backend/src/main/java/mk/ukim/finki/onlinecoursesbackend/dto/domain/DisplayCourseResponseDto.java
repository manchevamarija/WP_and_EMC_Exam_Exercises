package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;

public record DisplayCourseResponseDto(
    Long id,
    String title,
    String description,
    Long topicId,
    BigDecimal price,
    Integer capacity,
    LocalDate startDate,
    LocalDate endDate
) {
    public static DisplayCourseResponseDto from(Course course) {
        return new DisplayCourseResponseDto(
            course.getId(),
            course.getTitle(),
            course.getDescription(),
            course.getTopic().getId(),
            course.getPrice(),
            course.getCapacity(),
                course.getSchedule() !=null? course.getSchedule().getStartDate() : null,
                course.getSchedule() !=null? course.getSchedule().getEndDate() : null
                );
    }

    public static List<DisplayCourseResponseDto> from(List<Course> courses) {
        return courses
            .stream()
            .map(DisplayCourseResponseDto::from)
            .toList();
    }
}
