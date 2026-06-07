package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.TourSchedule;

public record DisplayTourResponseDto(
    Long id,
    String title,
    String description,
    Long destinationId,
    BigDecimal price,
    Integer capacity,
    LocalDate startDate,
    LocalDate endDate
) {
    public static DisplayTourResponseDto from(Tour tour) {
        TourSchedule schedule = tour.getSchedule();

        return new DisplayTourResponseDto(
            tour.getId(),
            tour.getTitle(),
            tour.getDescription(),
            tour.getDestination().getId(),
            tour.getPrice(),
            tour.getCapacity(),
                schedule != null ? schedule.getStartDate() : null,
                schedule != null ? schedule.getEndDate() : null
        );
    }

    public static List<DisplayTourResponseDto> from(List<Tour> tours) {
        return tours
            .stream()
            .map(DisplayTourResponseDto::from)
            .toList();
    }
}
