package mk.ukim.finki.traveltoursbackend.model.exception;

public class UserHasAlreadyReservedTourException extends RuntimeException {
    public UserHasAlreadyReservedTourException(Long tourId, String username) {
        super("The user with username '%s' has already reserved the tour with ID %s.".formatted(username, tourId));
    }
}
