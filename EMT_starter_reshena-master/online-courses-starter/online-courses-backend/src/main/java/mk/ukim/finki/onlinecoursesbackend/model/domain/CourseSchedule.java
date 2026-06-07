package mk.ukim.finki.onlinecoursesbackend.model.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class CourseSchedule {
    private LocalDate startDate;

    private LocalDate endDate;

    public CourseSchedule(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
