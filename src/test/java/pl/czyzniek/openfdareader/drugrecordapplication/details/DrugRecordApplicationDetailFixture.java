package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;

class DrugRecordApplicationDetailFixture {

    static DrugRecordApplicationDetail drugRecordApplicationDetail(String applicationNumber) {
        return new DrugRecordApplicationDetail(
            applicationNumber,
            "MANUFACTURER_NAME",
            "SUBSTANCE_NAME",
            new DrugRecordApplicationDetail.ProductNumbers(List.of("PRODUCT1", "PRODUCT2"))
        );
    }

    static DrugRecordApplicationDetail drugRecordApplicationDetail(String applicationNumber,
                                                                   String manufacturerName,
                                                                   String substanceName,
                                                                   List<String> productNumbers) {
        return new DrugRecordApplicationDetail(
            applicationNumber,
            manufacturerName,
            substanceName,
            new DrugRecordApplicationDetail.ProductNumbers(productNumbers)
        );
    }
}
