package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;

public record DisplayReservationResponseDto(
    Long id,
    DisplayTourResponseDto tour,
    String username
) {
    public static DisplayReservationResponseDto from(Reservation reservation) {
        return new DisplayReservationResponseDto(
            reservation.getId(),
            DisplayTourResponseDto.from(reservation.getTour()),
            reservation.getUser().getUsername()
        );
    }

    public static List<DisplayReservationResponseDto> from(List<Reservation> reservations) {
        return reservations
            .stream()
            .map(DisplayReservationResponseDto::from)
            .toList();
    }
}
