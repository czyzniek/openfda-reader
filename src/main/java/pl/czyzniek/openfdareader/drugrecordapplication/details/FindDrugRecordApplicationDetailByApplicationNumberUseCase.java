package pl.czyzniek.openfdareader.drugrecordapplication.details;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import pl.czyzniek.openfdareader.error.NotFoundError;
import pl.czyzniek.openfdareader.error.ServerError;
import pl.czyzniek.openfdareader.error.StructuredError;

@RequiredArgsConstructor
@Slf4j
class FindDrugRecordApplicationDetailByApplicationNumberUseCase {
    private final DrugRecordApplicationDetailRepository drugRecordApplicationDetailRepository;

    Either<StructuredError, Output> execute(Input input) {
        return Try.of(() -> drugRecordApplicationDetailRepository.findById(input.applicationNumber))
            .onFailure(ex -> log.error("Could not find drug application record detail by id!", ex))
            .toEither()
            .mapLeft(ex -> (StructuredError) new ServerError("Could not find drug application record detail!"))
            .flatMap(this::mapOutput);
    }

    private Either<StructuredError, Output> mapOutput(Optional<DrugRecordApplicationDetail> searchResult) {
        return Option.ofOptional(searchResult)
            .map(Output::new)
            .toEither(() -> new NotFoundError("Could not find drug application detail!"));

    }

    @Value
    static class Input {
        String applicationNumber;
    }

    @Value
    static class Output {
        DrugRecordApplicationDetail detail;
    }
}
