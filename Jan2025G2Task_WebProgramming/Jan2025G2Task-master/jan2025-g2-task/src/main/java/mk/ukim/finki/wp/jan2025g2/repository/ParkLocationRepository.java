package mk.ukim.finki.wp.jan2025g2.repository;

import mk.ukim.finki.wp.jan2025g2.model.ParkLocation;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkLocationRepository extends JpaSpecificationRepository<ParkLocation,Long> {
}
