package mk.ukim.finki.traveltoursbackend.model.exception;

public class TourCapacityIsFullException extends RuntimeException {
    public TourCapacityIsFullException(Long id) {
        super("The tour with ID %s has reached its capacity.".formatted(id));
    }
}
