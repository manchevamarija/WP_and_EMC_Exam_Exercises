package mk.ukim.finki.onlinecoursesbackend.service.application.impl;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayNotificationResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.service.application.NotificationApplicationService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationApplicationServiceImpl implements NotificationApplicationService {
    private final NotificationService notificationService;

    public NotificationApplicationServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public List<DisplayNotificationResponseDto> findAllForUser(User user) {
        return DisplayNotificationResponseDto.from(notificationService.findAllByUser(user));
    }
}
