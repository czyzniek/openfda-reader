package pl.czyzniek.openfdareader.error;

import lombok.Getter;

public class StructuredErrorException extends RuntimeException {
    @Getter
    private final StructuredError structuredError;

    public StructuredErrorException(StructuredError error) {
        super(error.getMessage());
        this.structuredError = error;
    }
}
