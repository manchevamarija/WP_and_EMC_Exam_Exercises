package mk.ukim.finki.onlinecoursesbackend.repository;

import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
