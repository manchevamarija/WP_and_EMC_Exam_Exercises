package mk.ukim.finki.onlinecoursesbackend.service.domain;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Notification;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;

public interface NotificationService {
    List<Notification> findAllByUser(User user);
}
