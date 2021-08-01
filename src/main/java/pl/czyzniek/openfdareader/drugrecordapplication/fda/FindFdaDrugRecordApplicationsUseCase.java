package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import io.vavr.control.Either;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import pl.czyzniek.openfdareader.error.StructuredError;

@RequiredArgsConstructor
class FindFdaDrugRecordApplicationsUseCase {
    private final DrugRecordApplicationStore drugRecordApplicationStore;

    Either<StructuredError, Output> execute(Input input) {
        return drugRecordApplicationStore.findDrugRecordApplications(input.manufacturerName, input.brandName, input.page)
            .map(Slice::getContent)
            .map(Output::new);
    }

    @Value
    static class Input {
        String manufacturerName;
        String brandName;
        Pageable page;
    }

    @Value
    static class Output {
        List<DrugRecordApplication> applications;
    }
}
