package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
class DrugRecordApplicationDetail {
    @Id
    String applicationNumber;
    String manufacturerName;
    String substanceName;
    ProductNumbers productNumbers;

    static DrugRecordApplicationDetail of(String applicationNumber,
                                          String manufacturerName,
                                          String substanceName,
                                          List<String> productNumbers) {
        return new DrugRecordApplicationDetail(
            applicationNumber,
            manufacturerName,
            substanceName,
            new ProductNumbers(productNumbers)
        );
    }

    @Value
    static class ProductNumbers {
        List<String> values;
    }
}
