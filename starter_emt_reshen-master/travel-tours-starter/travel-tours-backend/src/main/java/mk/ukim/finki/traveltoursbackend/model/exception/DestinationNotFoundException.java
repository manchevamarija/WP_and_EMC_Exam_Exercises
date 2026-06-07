package mk.ukim.finki.traveltoursbackend.model.exception;

public class DestinationNotFoundException extends RuntimeException {
    public DestinationNotFoundException(Long id) {
        super("The destination with ID '%s' does not exist.".formatted(id));
    }
}
