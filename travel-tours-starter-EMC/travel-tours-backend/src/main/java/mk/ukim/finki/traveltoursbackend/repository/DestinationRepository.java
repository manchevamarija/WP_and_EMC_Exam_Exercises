package mk.ukim.finki.traveltoursbackend.repository;

import mk.ukim.finki.traveltoursbackend.model.domain.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
