package mk.ukim.finki.traveltoursbackend.service.application;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayNotificationResponseDto;
import mk.ukim.finki.traveltoursbackend.model.domain.User;

public interface NotificationApplicationService {
    List<DisplayNotificationResponseDto> findAllForUser(User user);
}
