package mk.ukim.finki.onlinecoursesbackend.model.exception;

public class UserIsAlreadyEnrolledInCourseException extends RuntimeException {
    public UserIsAlreadyEnrolledInCourseException(Long courseId, String username) {
        super("The user with username '%s' is already enrolled in course with ID %s.".formatted(username, courseId));
    }
}
