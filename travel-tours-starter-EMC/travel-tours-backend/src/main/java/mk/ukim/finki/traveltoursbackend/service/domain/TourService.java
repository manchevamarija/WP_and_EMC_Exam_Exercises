package mk.ukim.finki.traveltoursbackend.service.domain;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;

public interface TourService {
    List<Tour> findAll();

    Optional<Tour> findById(Long id);

    Optional<Tour> findWithLockById(Long id);

    Tour create(Tour tour);

    Optional<Tour> update(Long id, Tour tour);

    Optional<Tour> deleteById(Long id);
}
