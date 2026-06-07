package mk.ukim.finki.traveltoursbackend.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findTopByOrderByIdDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Tour> findWithLockById(Long id);

}
