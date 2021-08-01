package pl.czyzniek.openfdareader.error;

import lombok.Value;

@Value
public class ValidationError implements StructuredError {
    ErrorCode code = ErrorCode.VALIDATION_ERROR;
    String message;
}
