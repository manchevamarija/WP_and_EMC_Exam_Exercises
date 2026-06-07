package mk.ukim.finki.traveltoursbackend.service.domain;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;

public interface DestinationService {
    List<Destination> findAll();

    Optional<Destination> findById(Long id);

    Destination create(Destination destination);

    Optional<Destination> update(Long id, Destination destination);

    Optional<Destination> deleteById(Long id);
}
