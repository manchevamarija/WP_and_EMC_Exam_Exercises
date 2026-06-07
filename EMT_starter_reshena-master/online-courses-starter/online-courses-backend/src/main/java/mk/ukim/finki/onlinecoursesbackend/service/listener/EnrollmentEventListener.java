package mk.ukim.finki.onlinecoursesbackend.service.listener;

import mk.ukim.finki.onlinecoursesbackend.model.domain.Notification;
import mk.ukim.finki.onlinecoursesbackend.model.event.UserEnrolledInCourseEvent;
import mk.ukim.finki.onlinecoursesbackend.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EnrollmentEventListener {
    private final NotificationRepository notificationRepository;

    public EnrollmentEventListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @EventListener
    public void onUserEnrolledInCourse(UserEnrolledInCourseEvent event) {
        String message = "You have been successfully enrolled in course '%s' (ID %s).".formatted(
                event.getCourseTitle(),
                event.getCourseId()
        );
        notificationRepository.save(new Notification(event.getUser(), message));
    }
}
