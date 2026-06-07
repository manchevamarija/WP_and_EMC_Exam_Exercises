package mk.ukim.finki.traveltoursbackend.service.listener;

import mk.ukim.finki.traveltoursbackend.model.domain.Notification;
import mk.ukim.finki.traveltoursbackend.model.event.UserReservedTourEvent;
import mk.ukim.finki.traveltoursbackend.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReservationEventListener {
    private final NotificationRepository notificationRepository;

    public ReservationEventListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onUserReservedTour(UserReservedTourEvent event) {
        String message = "You have successfully reserved the tour '%s' (ID %s).".formatted(
            event.getTourTitle(),
            event.getTourId()
        );
       notificationRepository.save(new Notification(
               event.getUser(),
               message));
    }
}
