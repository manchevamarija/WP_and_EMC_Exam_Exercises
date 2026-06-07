package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.time.LocalDateTime;
import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Notification;

public record DisplayNotificationResponseDto(
    Long id,
    String message,
    String username,
    LocalDateTime createdAt
) {
    public static DisplayNotificationResponseDto from(Notification notification) {
        return new DisplayNotificationResponseDto(
            notification.getId(),
            notification.getMessage(),
            notification.getUser().getUsername(),
            notification.getCreatedAt()
        );
    }

    public static List<DisplayNotificationResponseDto> from(List<Notification> notifications) {
        return notifications
            .stream()
            .map(DisplayNotificationResponseDto::from)
            .toList();
    }
}
