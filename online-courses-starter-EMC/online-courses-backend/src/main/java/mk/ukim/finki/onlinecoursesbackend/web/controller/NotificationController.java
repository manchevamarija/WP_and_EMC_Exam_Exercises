package mk.ukim.finki.onlinecoursesbackend.web.controller;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayNotificationResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.service.application.NotificationApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/notifications")
@RestController
public class NotificationController {
    private final NotificationApplicationService notificationApplicationService;

    public NotificationController(NotificationApplicationService notificationApplicationService) {
        this.notificationApplicationService = notificationApplicationService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/me")
    public ResponseEntity<List<DisplayNotificationResponseDto>> findAllByUser(
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(notificationApplicationService.findAllForUser(user));
    }
}
