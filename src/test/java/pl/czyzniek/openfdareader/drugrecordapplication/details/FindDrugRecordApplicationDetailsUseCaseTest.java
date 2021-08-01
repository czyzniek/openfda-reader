package pl.czyzniek.openfdareader.drugrecordapplication.details;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class FindDrugRecordApplicationDetailsUseCaseTest {
    private static final String EMPTY_MANUFACTURER_NAME = "emptyManufacturerName";

    final DrugRecordApplicationDetailsConfiguration configuration = new DrugRecordApplicationDetailsConfiguration();
    final FindDrugRecordApplicationDetailsUseCase useCase =
        configuration.findDrugRecordApplicationDetailsUseCase(new TestDrugRecordApplicationDetailRepository());

    @Test
    void shouldFindDrugRecordApplicationDetail(SoftAssertions softly) {
        //given
        final var input = new FindDrugRecordApplicationDetailsUseCase.Input(
            "ManufacturerName", "SubstanceName", List.of("Product1", "Product2"));

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasRightValueSatisfying(output ->
            softly.assertThat(output.getDetails()).satisfies(details -> {
                softly.assertThat(details).hasSize(1);
                softly.assertThat(details.get(0)).satisfies(drugRecordApplicationDetail -> {
                    softly.assertThat(drugRecordApplicationDetail.getApplicationNumber()).isEqualTo("applicationNumber1");
                    softly.assertThat(drugRecordApplicationDetail.getManufacturerName()).isEqualTo("ManufacturerName");
                    softly.assertThat(drugRecordApplicationDetail.getSubstanceName()).isEqualTo("SubstanceName");
                    softly.assertThat(drugRecordApplicationDetail.getProductNumbers().getValues()).containsExactlyInAnyOrder("Product1", "Product2");
                });
            }));
    }

    @Test
    void shouldHandleNotFindingAnyDrugRecordApplicationDetails(SoftAssertions softly) {
        //given
        final var input = new FindDrugRecordApplicationDetailsUseCase.Input(
            EMPTY_MANUFACTURER_NAME, null, null);

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasRightValueSatisfying(output ->
            softly.assertThat(output.getDetails()).hasSize(0));
    }

    private static class TestDrugRecordApplicationDetailRepository extends TestBaseDrugRecordApplicationDetailRepository {

        @Override
        public List<DrugRecordApplicationDetail> findAllDrugRecordApplicationDetail(String manufacturerName, String substanceName, String productNumbers) {
            if (EMPTY_MANUFACTURER_NAME.equals(manufacturerName)) {
                return List.of();
            }
            return List.of(
                DrugRecordApplicationDetailFixture.drugRecordApplicationDetail(
                    "applicationNumber1", manufacturerName, substanceName, List.of(productNumbers.split(","))));
        }
    }
}