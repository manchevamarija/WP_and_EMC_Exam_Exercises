package mk.ukim.finki.traveltoursbackend.model.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class TourSchedule {
    private LocalDate startDate;

    private LocalDate endDate;

    public TourSchedule(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
