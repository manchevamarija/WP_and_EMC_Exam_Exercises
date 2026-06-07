package mk.ukim.finki.onlinecoursesbackend.service.domain.impl;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Notification;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.repository.NotificationRepository;
import mk.ukim.finki.onlinecoursesbackend.service.domain.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> findAllByUser(User user) {
        return notificationRepository.findAllByUserOrderByCreatedAtDesc(user);
    }
}
