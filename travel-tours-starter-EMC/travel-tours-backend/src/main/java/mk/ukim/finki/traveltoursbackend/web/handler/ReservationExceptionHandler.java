package mk.ukim.finki.traveltoursbackend.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.traveltoursbackend.dto.error.ApiErrorResponseDto;
import mk.ukim.finki.traveltoursbackend.model.exception.TourCapacityIsFullException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasAlreadyReservedTourException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasNotReservedTourException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ReservationExceptionHandler extends AbstractExceptionHandler {
    @ExceptionHandler(UserHasAlreadyReservedTourException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUserIsAlreadyReserved(
        UserHasAlreadyReservedTourException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(UserHasNotReservedTourException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUserIsNotReserved(
        UserHasNotReservedTourException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(TourCapacityIsFullException.class)
    public ResponseEntity<ApiErrorResponseDto> handleTourCapacityIsFull(
        TourCapacityIsFullException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }
}