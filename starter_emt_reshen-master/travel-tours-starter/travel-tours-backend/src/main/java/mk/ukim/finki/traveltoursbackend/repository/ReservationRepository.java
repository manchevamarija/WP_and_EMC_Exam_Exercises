package mk.ukim.finki.traveltoursbackend.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @EntityGraph(attributePaths = {"tour", "tour.destination", "user"})
    List<Reservation> findAllByUser(User user);

    Boolean existsByTourAndUser(Tour tour, User user);

    Integer countByTour(Tour tour);

    Optional<Reservation> findByTourAndUser(Tour tour, User customer);
}
