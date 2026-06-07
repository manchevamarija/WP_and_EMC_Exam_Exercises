package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;

public record DisplayReservationDetailsResponseDto(
    Long id,
    DisplayTourDetailsResponseDto tour,
    String username
) {
    public static DisplayReservationDetailsResponseDto from(Reservation reservation) {
        return new DisplayReservationDetailsResponseDto(
            reservation.getId(),
            DisplayTourDetailsResponseDto.from(reservation.getTour()),
            reservation.getUser().getUsername()
        );
    }

    public static List<DisplayReservationDetailsResponseDto> from(List<Reservation> reservations) {
        return reservations
            .stream()
            .map(DisplayReservationDetailsResponseDto::from)
            .toList();
    }
}
