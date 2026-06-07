package mk.ukim.finki.onlinecoursesbackend.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.onlinecoursesbackend.dto.error.ApiErrorResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.exception.InvalidCredentialsException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UserNotFoundException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UsernameAlreadyExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class UserExceptionHandler extends AbstractExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleInvalidCredentials(
        InvalidCredentialsException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUserNotFound(
        UserNotFoundException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUsernameAlreadyExists(
        UsernameAlreadyExistsException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
    }
}
