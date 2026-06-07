package mk.ukim.finki.onlinecoursesbackend.model.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("The course with ID %s does not exist.".formatted(id));
    }
}
