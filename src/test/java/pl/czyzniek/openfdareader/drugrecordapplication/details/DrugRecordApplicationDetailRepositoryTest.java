package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {DrugRecordApplicationDetailsConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureJdbc
class DrugRecordApplicationDetailRepositoryTest {

    @Autowired
    DrugRecordApplicationDetailRepository repository;

    @Test
    void shouldSaveDrugRecordApplicationDetail(SoftAssertions softly) {
        //given
        final var detail = DrugRecordApplicationDetail.of(
            "APPLICATION_NUMBER",
            "MANUFACTURER_NAME",
            "SUBSTANCE_NAME",
            List.of("PRODUCT1", "PRODUCT2", "PRODUCT 3")
        );

        //and
        repository.insert(detail);

        //when
        final var foundDetail = repository.findById("APPLICATION_NUMBER");

        //then
        softly.assertThat(foundDetail).hasValueSatisfying(drugRecordApplicationDetail -> {
            softly.assertThat(drugRecordApplicationDetail.getApplicationNumber()).isEqualTo("APPLICATION_NUMBER");
            softly.assertThat(drugRecordApplicationDetail.getManufacturerName()).isEqualTo("MANUFACTURER_NAME");
            softly.assertThat(drugRecordApplicationDetail.getSubstanceName()).isEqualTo("SUBSTANCE_NAME");
            softly.assertThat(drugRecordApplicationDetail.getProductNumbers().getValues()).containsExactlyInAnyOrder("PRODUCT1", "PRODUCT2", "PRODUCT 3");
        });
    }
}