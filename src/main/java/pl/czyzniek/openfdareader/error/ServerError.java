package pl.czyzniek.openfdareader.error;

import lombok.Value;

@Value
public class ServerError implements StructuredError {
    ErrorCode code = ErrorCode.SERVER_ERROR;
    String message;
}
