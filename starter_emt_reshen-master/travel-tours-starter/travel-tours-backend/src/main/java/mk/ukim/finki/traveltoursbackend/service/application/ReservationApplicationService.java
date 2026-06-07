package mk.ukim.finki.traveltoursbackend.service.application;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationResponseDto;
import mk.ukim.finki.traveltoursbackend.model.domain.User;

public interface ReservationApplicationService {
    List<DisplayReservationDetailsResponseDto> findAllByUserWithDetails(User user);

    Boolean existsByTourAndUser(Long tourId, User user);

    Integer getAvailableSpotsByTour(Long tourId);

    DisplayReservationResponseDto reserveByTourAndUser(Long tourId, User user);

    DisplayReservationResponseDto cancelByTourAndUser(Long tourId, User user);
}
