package mk.ukim.finki.onlinecoursesbackend.model.event;

import java.time.LocalDateTime;
import lombok.Getter;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;

@Getter
public class UserEnrolledInCourseEvent {
    private final User user;
    private final Long courseId;
    private final String courseTitle;
    private final LocalDateTime occurredOn;

    public UserEnrolledInCourseEvent(User user, Long courseId, String courseTitle) {
        this.user = user;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.occurredOn = LocalDateTime.now();
    }
}

