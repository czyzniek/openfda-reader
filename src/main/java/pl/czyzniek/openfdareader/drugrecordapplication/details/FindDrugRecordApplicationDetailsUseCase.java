package pl.czyzniek.openfdareader.drugrecordapplication.details;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import pl.czyzniek.openfdareader.error.ServerError;
import pl.czyzniek.openfdareader.error.StructuredError;

@RequiredArgsConstructor
@Slf4j
class FindDrugRecordApplicationDetailsUseCase {
    private final DrugRecordApplicationDetailRepository drugRecordApplicationDetailRepository;

    Either<? extends StructuredError, Output> execute(Input input) {
        return Try.of(() -> drugRecordApplicationDetailRepository.findAllDrugRecordApplicationDetail(
                input.manufacturerName, input.substanceName, joinProductNumbers(input.productNumbers)))
            .onFailure(ex -> log.error("Could not find drug record application details!", ex))
            .toEither()
            .mapLeft(ex -> new ServerError("Could not find drug record application details!"))
            .map(Output::new);
    }

    private String joinProductNumbers(List<String> productNumbers) {
        return Option.of(productNumbers)
            .map(numbers -> String.join(",", numbers))
            .getOrNull();
    }

    @Value
    static class Input {
        String manufacturerName;
        String substanceName;
        List<String> productNumbers;
    }

    @Value
    static class Output {
        List<DrugRecordApplicationDetail> details;
    }
}
