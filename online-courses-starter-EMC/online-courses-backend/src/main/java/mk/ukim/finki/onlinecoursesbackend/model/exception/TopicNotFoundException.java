package mk.ukim.finki.onlinecoursesbackend.model.exception;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(Long id) {
        super("The topic with ID '%s' does not exist.".formatted(id));
    }
}
