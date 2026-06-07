package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.domain.CourseSchedule;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;

public record CreateOrUpdateCourseRequestDto(
    @NotBlank
    String title,
    @NotBlank
    String description,
    Long topicId,
    @Positive
    BigDecimal price,
    @Positive
    Integer capacity,
    @FutureOrPresent
    LocalDate startDate,
    @Future
    LocalDate endDate
) {
    public Course toCourse(Topic topic) {
        CourseSchedule schedule = (startDate == null || endDate == null) ? null : new CourseSchedule(startDate,endDate);
        return new Course(title,description,topic,price,capacity,schedule);
    }
}
