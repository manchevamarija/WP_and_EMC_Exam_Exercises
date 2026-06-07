package mk.ukim.finki.onlinecoursesbackend.model.exception;

public class CourseCapacityIsFullException extends RuntimeException {
    public CourseCapacityIsFullException(Long id) {
        super("The course with ID %s has reached its capacity.".formatted(id));
    }
}
