package mk.ukim.finki.onlinecoursesbackend.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.onlinecoursesbackend.dto.error.ApiErrorResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseNotFoundException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.TopicNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CourseExceptionHandler extends AbstractExceptionHandler {
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleCourseNotFound(
        CourseNotFoundException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleTopicNotFound(
        TopicNotFoundException exception,
        HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }
}
