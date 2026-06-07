package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;

public record DisplayCourseDetailsResponseDto(
    Long id,
    String title,
    String description,
    DisplayTopicResponseDto topic,
    BigDecimal price,
    Integer capacity,
    LocalDate startDate,
    LocalDate endDate,
    Long durationInDays
) {
    public static DisplayCourseDetailsResponseDto from(Course course) {
        return new DisplayCourseDetailsResponseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                DisplayTopicResponseDto.from(course.getTopic()),
                course.getPrice(),
                course.getCapacity(),
                course.getSchedule() !=null? course.getSchedule().getStartDate() : null,
                course.getSchedule() !=null? course.getSchedule().getEndDate() : null,
                course.getSchedule() !=null? course.getSchedule().getDurationInDays() : null
                );
    }
}
