package me.jeongrae.chat.interfaces.web;

import me.jeongrae.chat.domain.shared.error.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handle(DomainException ex, Locale locale) {
        var code = ex.code();
        var status = switch (code.name()) {
            case "MESSAGE_ROOM_MISMATCH", "INVALID_PARTICIPANTS" -> HttpStatus.BAD_REQUEST;
            case "SENDER_NOT_PARTICIPANT" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        var userMessage = code.defaultMessage();

        var body = new ApiError(code.name(), userMessage, ex.details().asMap());
        return ResponseEntity.status(status).body(body);
    }

    public record ApiError(String code, String message, Map<String, Object> details) {
    }
}
