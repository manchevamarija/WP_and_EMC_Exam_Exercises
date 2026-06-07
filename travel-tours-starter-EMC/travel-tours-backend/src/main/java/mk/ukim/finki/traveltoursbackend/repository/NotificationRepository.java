package mk.ukim.finki.traveltoursbackend.repository;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Notification;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);

    User user(User user);
}
