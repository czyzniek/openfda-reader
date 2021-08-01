package pl.czyzniek.openfdareader.drugrecordapplication.details;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.czyzniek.openfdareader.error.StructuredError;

@ExtendWith(SoftAssertionsExtension.class)
class FindDrugRecordApplicationDetailByApplicationNumberUseCaseTest {
    private static final String EMPTY_APPLICATION_NUMBER = "EmptyApplicationNumber";

    final DrugRecordApplicationDetailsConfiguration configuration = new DrugRecordApplicationDetailsConfiguration();
    final FindDrugRecordApplicationDetailByApplicationNumberUseCase useCase =
        configuration.findDrugRecordApplicationDetailByApplicationNumberUseCase(new TestDrugRecordApplicationDetailRepository());

    @Test
    void shouldFindDrugRecordApplicationDetail(SoftAssertions softly) {
        //given
        final var input = new FindDrugRecordApplicationDetailByApplicationNumberUseCase.Input("applicationNumber");

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasRightValueSatisfying(output ->
            softly.assertThat(output.getDetail()).satisfies(drugRecordApplicationDetail -> {
                softly.assertThat(drugRecordApplicationDetail.getApplicationNumber()).isEqualTo("applicationNumber");
                softly.assertThat(drugRecordApplicationDetail.getManufacturerName()).isEqualTo("manufacturerName");
                softly.assertThat(drugRecordApplicationDetail.getSubstanceName()).isEqualTo("substanceName");
                softly.assertThat(drugRecordApplicationDetail.getProductNumbers().getValues()).containsExactlyInAnyOrder("PRODUCT1", "PRODUCT2");
            }));
    }

    @Test
    void shouldHandleNotFindingDrugRecordApplicationDetail(SoftAssertions softly) {
        //given
        final var input = new FindDrugRecordApplicationDetailByApplicationNumberUseCase.Input(EMPTY_APPLICATION_NUMBER);

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasLeftValueSatisfying(error -> {
            softly.assertThat(error.getCode()).isEqualTo(StructuredError.ErrorCode.NOT_FOUND_ERROR);
            softly.assertThat(error.getMessage()).isEqualTo("Could not find drug application detail!");
        });
    }

    private static class TestDrugRecordApplicationDetailRepository extends TestBaseDrugRecordApplicationDetailRepository {

        @Override
        public Optional<DrugRecordApplicationDetail> findById(String s) {
            if (EMPTY_APPLICATION_NUMBER.equals(s)) {
                return Optional.empty();
            }
            return Optional.of(
                DrugRecordApplicationDetailFixture.drugRecordApplicationDetail(
                    s, "manufacturerName", "substanceName", List.of("PRODUCT1", "PRODUCT2")));
        }
    }
}