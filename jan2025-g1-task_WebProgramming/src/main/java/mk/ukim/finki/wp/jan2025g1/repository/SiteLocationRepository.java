package mk.ukim.finki.wp.jan2025g1.repository;

import mk.ukim.finki.wp.jan2025g1.model.SiteLocation;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteLocationRepository extends JpaSpecificationRepository<SiteLocation,Long> {
}
