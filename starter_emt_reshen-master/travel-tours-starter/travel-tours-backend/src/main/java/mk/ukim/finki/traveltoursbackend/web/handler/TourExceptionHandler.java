package mk.ukim.finki.traveltoursbackend.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.traveltoursbackend.dto.error.ApiErrorResponseDto;
import mk.ukim.finki.traveltoursbackend.model.exception.TourNotFoundException;
import mk.ukim.finki.traveltoursbackend.model.exception.DestinationNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class TourExceptionHandler extends AbstractExceptionHandler {
    @ExceptionHandler(TourNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleTourNotFound(
        TourNotFoundException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(DestinationNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleDestinationNotFound(
        DestinationNotFoundException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }
}
