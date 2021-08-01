package pl.czyzniek.openfdareader.error;

import lombok.Value;

@Value
public class NotFoundError implements StructuredError {
    ErrorCode code = ErrorCode.NOT_FOUND_ERROR;
    String message;
}
