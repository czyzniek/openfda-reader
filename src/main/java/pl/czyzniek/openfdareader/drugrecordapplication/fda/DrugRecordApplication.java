package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import java.time.LocalDate;
import java.util.List;
import lombok.Value;

@Value
class DrugRecordApplication {
    String applicationNumber;
    String sponsorName;
    List<Submission> submissions;
    List<Product> products;

    @Value
    static class Submission {
        String type;
        String number;
        String status;
        LocalDate statusDate;
        String classCode;
        String classCodeDescription;
        List<ApplicationDoc> applicationDocs;

        @Value
        static class ApplicationDoc {
            String id;
            String url;
            LocalDate date;
            String type;
        }
    }

    @Value
    static class Product {
        String number;
        String referenceDrug;
        String brandName;
        List<ActiveIngredient> activeIngredients;
        String referenceStandard;
        String dosageForm;
        String route;
        String marketingStatus;

        @Value
        static class ActiveIngredient {
            String name;
            String strength;
        }
    }
}
