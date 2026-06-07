package mk.ukim.finki.traveltoursbackend.model.exception;

public class UserHasNotReservedTourException extends RuntimeException {
    public UserHasNotReservedTourException(Long tourId, String username) {
        super("The user with username '%s' has not reserved the tour with ID %s.".formatted(username, tourId));
    }
}
