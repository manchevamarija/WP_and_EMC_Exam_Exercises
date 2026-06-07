package mk.ukim.finki.traveltoursbackend.service.domain;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Notification;
import mk.ukim.finki.traveltoursbackend.model.domain.User;

public interface NotificationService {
    List<Notification> findAllByUser(User user);
}
