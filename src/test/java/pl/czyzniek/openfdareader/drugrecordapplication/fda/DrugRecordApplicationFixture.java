package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import java.time.LocalDate;
import java.util.List;

class DrugRecordApplicationFixture {

    static DrugRecordApplication drugRecordApplication(String applicationNumber) {
        return new DrugRecordApplication(
            applicationNumber,
            "SPONSOR_NAME",
            List.of(new DrugRecordApplication.Submission(
                "SUPPL",
                "5",
                "AP",
                LocalDate.parse("1988-01-22"),
                "MANUF (CA)",
                "Manufacturing (CMC)",
                List.of()
            )),
            List.of(new DrugRecordApplication.Product(
                "001",
                "No",
                "ALLOPURINOL",
                List.of(new DrugRecordApplication.Product.ActiveIngredient("ALLOPURINOL", "100MG")),
                "No",
                "TABLET",
                "ORAL",
                "Prescription"
            ))
        );
    }
}
