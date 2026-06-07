package mk.ukim.finki.traveltoursbackend.service.domain;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.model.domain.User;

public interface ReservationService {
    List<Reservation> findAll();

    List<Reservation> findAllByUser(User user);

    Boolean existsByTourAndUser(Tour tour, User user);

    Integer getAvailableSpotsByTour(Tour tour);

    Reservation reserveByTourAndUser(Tour tour, User user);

    Reservation cancelByTourAndUser(Tour tour, User user);
}
