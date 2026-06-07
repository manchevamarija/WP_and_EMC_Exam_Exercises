package mk.ukim.finki.traveltoursbackend.model.exception;

public class TourNotFoundException extends RuntimeException {
    public TourNotFoundException(Long id) {
        super("The tour with ID %s does not exist.".formatted(id));
    }
}
