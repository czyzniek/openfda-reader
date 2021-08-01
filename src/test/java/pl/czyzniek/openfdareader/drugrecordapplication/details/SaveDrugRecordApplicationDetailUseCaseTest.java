package pl.czyzniek.openfdareader.drugrecordapplication.details;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DuplicateKeyException;
import pl.czyzniek.openfdareader.error.StructuredError;

@ExtendWith(SoftAssertionsExtension.class)
class SaveDrugRecordApplicationDetailUseCaseTest {
    private static final String WRONG_APPLICATION_NUMBER = "WRONG_APPLICATION_NUMBER";
    private static final String DUPLICATED_APPLICATION_NUMBER = "DUPLICATED_APPLICATION_NUMBER";

    final DrugRecordApplicationDetailsConfiguration configuration = new DrugRecordApplicationDetailsConfiguration();
    final SaveDrugRecordApplicationDetailUseCase useCase =
        configuration.saveDrugRecordApplicationDetailUseCase(new TestDrugRecordApplicationDetailRepository());

    @Test
    void shouldSaveNewDrugRecordApplicationDetail() {
        //given
        final var input = new SaveDrugRecordApplicationDetailUseCase.Input(
            "APPLICATION_NUMBER",
            "MANUFACTURER_NAME",
            "SUBSTANCE_NAME",
            List.of("PRODUCT1", "PRODUCT2", "PRODUCT 3")
        );

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).isRight();
    }

    @Test
    void shouldHandleDuplicatedRecord(SoftAssertions softly) {
        //given
        final var input = new SaveDrugRecordApplicationDetailUseCase.Input(
            DUPLICATED_APPLICATION_NUMBER,
            "MANUFACTURER_NAME",
            "SUBSTANCE_NAME",
            List.of("PRODUCT1", "PRODUCT2", "PRODUCT 3")
        );

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasLeftValueSatisfying(error -> {
            softly.assertThat(error.getCode()).isEqualTo(StructuredError.ErrorCode.VALIDATION_ERROR);
            softly.assertThat(error.getMessage()).isEqualTo("Drug record application detail with given application number already exists!");
        });
    }

    @Test
    void shouldHandleCreationErrors(SoftAssertions softly) {
        //given
        final var input = new SaveDrugRecordApplicationDetailUseCase.Input(
            WRONG_APPLICATION_NUMBER,
            "MANUFACTURER_NAME",
            "SUBSTANCE_NAME",
            List.of("PRODUCT1", "PRODUCT2", "PRODUCT 3")
        );

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasLeftValueSatisfying(error -> {
            softly.assertThat(error.getCode()).isEqualTo(StructuredError.ErrorCode.SERVER_ERROR);
            softly.assertThat(error.getMessage()).isEqualTo("Could not save drug record application detail!");
        });
    }

    private static class TestDrugRecordApplicationDetailRepository extends TestBaseDrugRecordApplicationDetailRepository {

        @Override
        public DrugRecordApplicationDetail insert(DrugRecordApplicationDetail detail) {
            if (SaveDrugRecordApplicationDetailUseCaseTest.WRONG_APPLICATION_NUMBER.equals(detail.getApplicationNumber())) {
                throw new IllegalStateException("Cannot save!");
            }
            if (SaveDrugRecordApplicationDetailUseCaseTest.DUPLICATED_APPLICATION_NUMBER.equals(detail.getApplicationNumber())) {
                throw new IllegalStateException(new DuplicateKeyException("Already exists!"));
            }
            return detail;
        }
    }
}