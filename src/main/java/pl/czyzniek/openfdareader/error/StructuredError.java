package pl.czyzniek.openfdareader.error;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public interface StructuredError extends Serializable {
    long serialVersionUID = 2405172041950251807L;

    ErrorCode getCode();

    String getMessage();

    @RequiredArgsConstructor
    enum ErrorCode {
        VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
        NOT_FOUND_ERROR(HttpStatus.NOT_FOUND),
        SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

        @Getter
        private final HttpStatus status;
    }
}
