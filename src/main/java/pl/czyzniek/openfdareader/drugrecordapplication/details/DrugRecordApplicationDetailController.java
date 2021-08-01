package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.czyzniek.openfdareader.error.StructuredErrorException;

@RestController
@RequiredArgsConstructor
public class DrugRecordApplicationDetailController {
    private final SaveDrugRecordApplicationDetailUseCase saveDrugRecordApplicationDetailUseCase;
    private final FindDrugRecordApplicationDetailsUseCase findDrugRecordApplicationDetailsUseCase;
    private final FindDrugRecordApplicationDetailByApplicationNumberUseCase findDrugRecordApplicationDetailByApplicationNumberUseCase;

    @PostMapping(
        path = "/drug-record-application-details",
        consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDrugRecordApplicationDetail(@Valid @RequestBody CreateDrugRecordApplicationDetailRequest request) {
        saveDrugRecordApplicationDetailUseCase.execute(request.toInput())
            .getOrElseThrow(StructuredErrorException::new);
    }

    @GetMapping(path = "/drug-record-application-details/{applicationNumber}")
    public FoundDrugRecordApplicationDetail findDrugRecordApplicationDetailsByApplicationNumber(@PathVariable String applicationNumber) {
        return findDrugRecordApplicationDetailByApplicationNumberUseCase.execute(
                new FindDrugRecordApplicationDetailByApplicationNumberUseCase.Input(applicationNumber))
            .map(FindDrugRecordApplicationDetailByApplicationNumberUseCase.Output::getDetail)
            .map(detail -> new FoundDrugRecordApplicationDetail(
                detail.getApplicationNumber(),
                detail.getManufacturerName(),
                detail.getSubstanceName(),
                detail.getProductNumbers().getValues()
            ))
            .getOrElseThrow(StructuredErrorException::new);
    }

    @GetMapping(path = "/drug-record-application-details")
    public List<FoundDrugRecordApplicationDetail> searchForDrugRecordApplicationDetails(FindByDrugRecordApplicationDetailRequest request) {
        return findDrugRecordApplicationDetailsUseCase.execute(request.toInput())
            .map(FindDrugRecordApplicationDetailsUseCase.Output::getDetails)
            .map(details -> details.stream().map(FoundDrugRecordApplicationDetail::fromDomain).collect(Collectors.toList()))
            .getOrElseThrow(StructuredErrorException::new);
    }

    @Value
    public static class CreateDrugRecordApplicationDetailRequest {
        @NotNull
        String applicationNumber;
        @NotNull
        String manufacturerName;
        @NotNull
        String substanceName;
        @NotEmpty
        List<String> productNumbers;

        SaveDrugRecordApplicationDetailUseCase.Input toInput() {
            return new SaveDrugRecordApplicationDetailUseCase.Input(
                applicationNumber,
                manufacturerName,
                substanceName,
                productNumbers
            );
        }
    }

    @Value
    public static class FindByDrugRecordApplicationDetailRequest {
        String manufacturerName;
        String substanceName;
        List<String> productNumbers;

        FindDrugRecordApplicationDetailsUseCase.Input toInput() {
            return new FindDrugRecordApplicationDetailsUseCase.Input(manufacturerName, substanceName, productNumbers);
        }
    }

    @Value
    public static class FoundDrugRecordApplicationDetail {
        String applicationNumber;
        String manufacturerName;
        String substanceName;
        List<String> productNumbers;

        static FoundDrugRecordApplicationDetail fromDomain(DrugRecordApplicationDetail detail) {
            return new FoundDrugRecordApplicationDetail(
                detail.getApplicationNumber(),
                detail.getManufacturerName(),
                detail.getSubstanceName(),
                detail.getProductNumbers().getValues()
            );
        }
    }

}
