package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.czyzniek.openfdareader.error.StructuredError;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {"fda.baseUrl=http://localhost:${wiremock.server.port}", "springdoc.api-docs.enabled=false"})
@AutoConfigureWireMock(port = 0)
class OpenFdaDrugRecordApplicationStoreTest {

    @Autowired
    DrugRecordApplicationStore drugRecordApplicationStore;

    @Test
    void shouldFetchDrugRecordApplicationsFromOpenFdaWithoutAnySearchParameters(SoftAssertions softly) {
        //when
        final var result = drugRecordApplicationStore.findDrugRecordApplications(null, null, null);

        //then
        assertThat(result).hasRightValueSatisfying(drugRecordApplicationsPage ->
            softly.assertThat(drugRecordApplicationsPage.getContent()).satisfies(drugRecordApplications -> {
                softly.assertThat(drugRecordApplications.size()).isEqualTo(1);
                softly.assertThat(drugRecordApplications.get(0).getApplicationNumber()).isEqualTo("ANDA076242");
            }));
    }

    @Test
    void shouldFetchDrugRecordApplicationsFromOpenFdaWithSearchManufacturerNameParameters(SoftAssertions softly) {
        //given
        final var manufacturerName = "Manufacturer Name";

        //when
        final var result = drugRecordApplicationStore.findDrugRecordApplications(manufacturerName, null, null);

        //then
        assertThat(result).hasRightValueSatisfying(drugRecordApplicationsPage ->
            softly.assertThat(drugRecordApplicationsPage.getContent()).satisfies(drugRecordApplications -> {
                softly.assertThat(drugRecordApplications.size()).isEqualTo(1);
                softly.assertThat(drugRecordApplications.get(0).getApplicationNumber()).isEqualTo("ANDA076242");
            }));
    }

    @Test
    void shouldFetchDrugRecordApplicationsFromOpenFdaWithSearchManufacturerNameAndBrandNameParameters(SoftAssertions softly) {
        //given
        final var manufacturerName = "Manufacturer Name";
        final var brandName = "Brand Name";

        //when
        final var result = drugRecordApplicationStore.findDrugRecordApplications(manufacturerName, brandName, null);

        //then
        assertThat(result).hasRightValueSatisfying(drugRecordApplicationsPage ->
            softly.assertThat(drugRecordApplicationsPage.getContent()).satisfies(drugRecordApplications -> {
                softly.assertThat(drugRecordApplications.size()).isEqualTo(1);
                softly.assertThat(drugRecordApplications.get(0).getApplicationNumber()).isEqualTo("ANDA076242");
            }));
    }

    @Test
    void shouldFetchDrugRecordApplicationsFromOpenFdaWithSearchManufacturerNameAndBrandNameAndPageParameters(SoftAssertions softly) {
        //given
        final var manufacturerName = "Manufacturer Name";
        final var brandName = "Brand Name";
        final var page = PageRequest.of(1, 1);

        //when
        final var result = drugRecordApplicationStore.findDrugRecordApplications(manufacturerName, brandName, page);

        //then
        assertThat(result).hasRightValueSatisfying(drugRecordApplicationsPage ->
            softly.assertThat(drugRecordApplicationsPage.getContent()).satisfies(drugRecordApplications -> {
                softly.assertThat(drugRecordApplications.size()).isEqualTo(1);
                softly.assertThat(drugRecordApplications.get(0).getApplicationNumber()).isEqualTo("ANDA076242");
            }));
    }

    @Test
    void shouldFetchDrugRecordApplicationsFromOpenFdaWithSearchManufacturerNameAndBrandNameAndPageAndSortParameters(SoftAssertions softly) {
        //given
        final var manufacturerName = "Manufacturer Name";
        final var brandName = "Brand Name";
        final var page = PageRequest.of(1, 1, Sort.by("application_name"));

        //when
        final var result = drugRecordApplicationStore.findDrugRecordApplications(manufacturerName, brandName, page);

        //then
        assertThat(result).hasRightValueSatisfying(drugRecordApplicationsPage ->
            softly.assertThat(drugRecordApplicationsPage.getContent()).satisfies(drugRecordApplications -> {
                softly.assertThat(drugRecordApplications.size()).isEqualTo(1);
                softly.assertThat(drugRecordApplications.get(0).getApplicationNumber()).isEqualTo("ANDA076242");
            }));
    }

    @Test
    void shouldHandleErrorWhenDrugRecordApplicationsCouldNotBeFound(SoftAssertions softly) {
        //given
        final var manufacturerName = "WRONG";

        //when
        final var result = drugRecordApplicationStore.findDrugRecordApplications(manufacturerName, null, null);

        //then
        assertThat(result).hasLeftValueSatisfying(error -> {
            softly.assertThat(error.getCode()).isEqualTo(StructuredError.ErrorCode.NOT_FOUND_ERROR);
            softly.assertThat(error.getMessage()).isEqualTo("Could not find drug application records!");
        });
    }
}