package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import pl.czyzniek.openfdareader.error.StructuredErrorException;

@RestController
@RequiredArgsConstructor
public class DrugRecordApplicationController implements DrugRecordApplicationControllerDefinition {
    private final FindFdaDrugRecordApplicationsUseCase findFdaDrugRecordApplicationsUseCase;

    @Override
    public List<DrugRecordApplication> findFdaDrugRecordApplications(String manufacturerName,
                                                                     String brandName,
                                                                     Pageable pageable) {
        final var input = new FindFdaDrugRecordApplicationsUseCase.Input(manufacturerName, brandName, pageable);
        return findFdaDrugRecordApplicationsUseCase.execute(input)
            .map(FindFdaDrugRecordApplicationsUseCase.Output::getApplications)
            .getOrElseThrow(StructuredErrorException::new);
    }
}
