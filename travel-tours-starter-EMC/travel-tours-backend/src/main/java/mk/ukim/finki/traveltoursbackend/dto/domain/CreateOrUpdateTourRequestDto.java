package mk.ukim.finki.traveltoursbackend.dto.domain;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.TourSchedule;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;

public record CreateOrUpdateTourRequestDto(
    @NotBlank
    String title,
    @NotBlank
    String description,
    Long destinationId,
    @Positive
    BigDecimal price,
    @Positive
    Integer capacity,
    @FutureOrPresent
    LocalDate startDate,
    @Future
    LocalDate endDate
) {
    public Tour toTour(Destination destination) {
        return new Tour(
                title,
                description,
                destination,
                price,
                capacity
        );
    }
}
