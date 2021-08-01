package pl.czyzniek.openfdareader.error;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiExceptionHandler;

class StructuredErrorExceptionHandler implements ApiExceptionHandler {

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof StructuredErrorException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        final var structuredErrorEx = (StructuredErrorException) exception;
        return new ApiErrorResponse(structuredErrorEx.getStructuredError().getCode().getStatus(),
            structuredErrorEx.getStructuredError().getCode().toString(),
            structuredErrorEx.getStructuredError().getMessage());
    }
}
