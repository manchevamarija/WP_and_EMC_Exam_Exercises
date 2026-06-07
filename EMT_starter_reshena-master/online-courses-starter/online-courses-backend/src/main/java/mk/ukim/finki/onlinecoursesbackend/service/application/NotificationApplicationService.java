package mk.ukim.finki.onlinecoursesbackend.service.application;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayNotificationResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;

public interface NotificationApplicationService {
    List<DisplayNotificationResponseDto> findAllForUser(User user);
}
