package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.TourSchedule;

public record DisplayTourDetailsResponseDto(
    Long id,
    String title,
    String description,
    DisplayDestinationResponseDto destination,
    BigDecimal price,
    Integer capacity,
    LocalDate startDate,
    LocalDate endDate,
    Long durationInDays
) {
    public static DisplayTourDetailsResponseDto from(Tour tour) {
        TourSchedule schedule = tour.getSchedule();

        return new DisplayTourDetailsResponseDto(
                tour.getId(),
                tour.getTitle(),
                tour.getDescription(),
                DisplayDestinationResponseDto.from(tour.getDestination()),
                tour.getPrice(),
                tour.getCapacity(),
                schedule != null ? schedule.getStartDate() : null,
                schedule != null ? schedule.getEndDate() : null,
                schedule != null ? schedule.getDurationInDays() : null
        );
    }
}
