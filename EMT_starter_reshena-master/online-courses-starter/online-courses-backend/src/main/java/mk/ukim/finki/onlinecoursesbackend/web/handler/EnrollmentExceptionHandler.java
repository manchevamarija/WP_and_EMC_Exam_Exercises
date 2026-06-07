package mk.ukim.finki.onlinecoursesbackend.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.onlinecoursesbackend.dto.error.ApiErrorResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseCapacityIsFullException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UserIsAlreadyEnrolledInCourseException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UserIsNotEnrolledInCourseException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class EnrollmentExceptionHandler extends AbstractExceptionHandler {
    @ExceptionHandler(UserIsAlreadyEnrolledInCourseException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUserIsAlreadyEnrolled(
        UserIsAlreadyEnrolledInCourseException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(UserIsNotEnrolledInCourseException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUserIsNotEnrolled(
        UserIsNotEnrolledInCourseException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(CourseCapacityIsFullException.class)
    public ResponseEntity<ApiErrorResponseDto> handleCourseCapacityIsFull(
        CourseCapacityIsFullException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }
}