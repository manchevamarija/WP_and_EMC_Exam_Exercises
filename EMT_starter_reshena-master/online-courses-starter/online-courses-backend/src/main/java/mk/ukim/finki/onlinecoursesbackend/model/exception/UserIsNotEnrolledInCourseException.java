package mk.ukim.finki.onlinecoursesbackend.model.exception;

public class UserIsNotEnrolledInCourseException extends RuntimeException {
    public UserIsNotEnrolledInCourseException(Long courseId, String username) {
        super("The user with username '%s' is not enrolled in course with ID %s.".formatted(username, courseId));
    }
}
