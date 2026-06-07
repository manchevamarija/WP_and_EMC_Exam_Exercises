package mk.ukim.finki.traveltoursbackend.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.traveltoursbackend.dto.error.ApiErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractExceptionHandler {
    protected ResponseEntity<ApiErrorResponseDto> buildResponse(
        HttpStatus status,
        String message,
        HttpServletRequest request
    ) {
        return new ResponseEntity<>(
            new ApiErrorResponseDto(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
            ),
            status
        );
    }
}