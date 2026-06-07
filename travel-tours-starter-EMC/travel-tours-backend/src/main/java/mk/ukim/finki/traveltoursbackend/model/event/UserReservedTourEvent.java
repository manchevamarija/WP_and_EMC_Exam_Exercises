package mk.ukim.finki.traveltoursbackend.model.event;

import java.time.LocalDateTime;
import lombok.Getter;
import mk.ukim.finki.traveltoursbackend.model.domain.User;

@Getter
public class UserReservedTourEvent {
    private final User user;
    private final Long tourId;
    private final String tourTitle;
    private final LocalDateTime occurredOn;

    public UserReservedTourEvent(User user, Long tourId, String tourTitle) {
        this.user = user;
        this.tourId = tourId;
        this.tourTitle = tourTitle;
        this.occurredOn = LocalDateTime.now();
    }
}

