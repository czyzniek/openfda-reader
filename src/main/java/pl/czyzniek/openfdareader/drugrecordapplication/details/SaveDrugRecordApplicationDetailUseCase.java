package pl.czyzniek.openfdareader.drugrecordapplication.details;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import pl.czyzniek.openfdareader.error.ServerError;
import pl.czyzniek.openfdareader.error.StructuredError;
import pl.czyzniek.openfdareader.error.ValidationError;

@RequiredArgsConstructor
@Slf4j
class SaveDrugRecordApplicationDetailUseCase {
    private final DrugRecordApplicationDetailRepository drugRecordApplicationDetailRepository;

    Either<StructuredError, Void> execute(Input input) {
        return Try.of(() -> drugRecordApplicationDetailRepository.insert(input.toDomain()))
            .onFailure(ex -> log.error("Could not save drug record application detail!", ex))
            .toEither()
            .mapLeft(ex -> Match(ex.getCause()).of(
                Case($(instanceOf(DuplicateKeyException.class)),
                    duplicateKey -> new ValidationError("Drug record application detail with given application number already exists!")),
                Case($(), cause -> new ServerError("Could not save drug record application detail!"))
            ))
            .map(savedDetails -> null);
    }

    @Value
    static class Input {
        String applicationNumber;
        String manufacturerName;
        String substanceName;
        List<String> productNumbers;

        DrugRecordApplicationDetail toDomain() {
            return DrugRecordApplicationDetail.of(
                applicationNumber,
                manufacturerName,
                substanceName,
                productNumbers
            );
        }
    }
}
