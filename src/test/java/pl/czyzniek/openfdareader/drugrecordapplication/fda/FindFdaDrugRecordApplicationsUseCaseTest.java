package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

import io.vavr.control.Either;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.czyzniek.openfdareader.error.ServerError;
import pl.czyzniek.openfdareader.error.StructuredError;

@ExtendWith(SoftAssertionsExtension.class)
class FindFdaDrugRecordApplicationsUseCaseTest {
    private static final String CORRECT_MANUFACTURER_NAME = "manufacturerName";
    private static final String INCORRECT_MANUFACTURER_NAME = "WrongManufacturerName";
    private static final String CORRECT_BRAND_NAME = "brandName";
    private static final String CORRECT_APPLICATION_NAME = "applicationName";

    private final FdaDrugRecordApplicationConfiguration configuration = new FdaDrugRecordApplicationConfiguration();
    private final FindFdaDrugRecordApplicationsUseCase useCase = configuration.findFdaDrugRecordApplicationsUseCase(new TestDrugRecordApplicationStore());

    @Test
    public void shouldFindDrugRecordApplication(SoftAssertions softly) {
        //given
        final var input = new FindFdaDrugRecordApplicationsUseCase.Input(CORRECT_MANUFACTURER_NAME, CORRECT_BRAND_NAME, null);

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasRightValueSatisfying(output -> {
            softly.assertThat(output.getApplications().size()).isEqualTo(1);
            softly.assertThat(output.getApplications().get(0)).satisfies(drugRecordApplication -> {
                softly.assertThat(drugRecordApplication.getApplicationNumber()).isEqualTo(CORRECT_APPLICATION_NAME);
                softly.assertThat(drugRecordApplication.getSponsorName()).isEqualTo("SPONSOR_NAME");
                softly.assertThat(drugRecordApplication.getSubmissions().size()).isEqualTo(1);
                softly.assertThat(drugRecordApplication.getSubmissions().get(0)).satisfies(submission -> {
                    softly.assertThat(submission.getType()).isEqualTo("SUPPL");
                    softly.assertThat(submission.getNumber()).isEqualTo("5");
                    softly.assertThat(submission.getStatus()).isEqualTo("AP");
                    softly.assertThat(submission.getStatusDate()).isEqualTo(LocalDate.of(1988, 1, 22));
                    softly.assertThat(submission.getClassCode()).isEqualTo("MANUF (CA)");
                    softly.assertThat(submission.getClassCodeDescription()).isEqualTo("Manufacturing (CMC)");
                    softly.assertThat(submission.getApplicationDocs()).isEmpty();
                });
                softly.assertThat(drugRecordApplication.getProducts().size()).isEqualTo(1);
                softly.assertThat(drugRecordApplication.getProducts().get(0)).satisfies(product -> {
                    softly.assertThat(product.getNumber()).isEqualTo("001");
                    softly.assertThat(product.getReferenceDrug()).isEqualTo("No");
                    softly.assertThat(product.getBrandName()).isEqualTo("ALLOPURINOL");
                    softly.assertThat(product.getActiveIngredients().size()).isEqualTo(1);
                    softly.assertThat(product.getActiveIngredients().get(0)).satisfies(activeIngredient -> {
                        softly.assertThat(activeIngredient.getName()).isEqualTo("ALLOPURINOL");
                        softly.assertThat(activeIngredient.getStrength()).isEqualTo("100MG");
                    });
                    softly.assertThat(product.getReferenceStandard()).isEqualTo("No");
                    softly.assertThat(product.getDosageForm()).isEqualTo("TABLET");
                    softly.assertThat(product.getRoute()).isEqualTo("ORAL");
                    softly.assertThat(product.getMarketingStatus()).isEqualTo("Prescription");
                });
            });
        });
    }

    @Test
    void shouldReturnErrorWhenThereAreProblemsWithFindingApplication(SoftAssertions softly) {
        //given
        final var input = new FindFdaDrugRecordApplicationsUseCase.Input(INCORRECT_MANUFACTURER_NAME, CORRECT_BRAND_NAME, null);

        //when
        final var result = useCase.execute(input);

        //then
        assertThat(result).hasLeftValueSatisfying(error -> {
            softly.assertThat(error.getCode()).isEqualTo(StructuredError.ErrorCode.SERVER_ERROR);
            softly.assertThat(error.getMessage()).isEqualTo("Could not find drug record applications!");
        });
    }

    private static class TestDrugRecordApplicationStore implements DrugRecordApplicationStore {

        @Override
        public Either<StructuredError, ? extends Page<DrugRecordApplication>> findDrugRecordApplications(String manufacturerName,
                                                                                                         String brandName,
                                                                                                         Pageable page) {
            if (INCORRECT_MANUFACTURER_NAME.equals(manufacturerName)) {
                return Either.left(new ServerError("Could not find drug record applications!"));
            }
            return Either.right(new PageImpl<>(List.of(DrugRecordApplicationFixture.drugRecordApplication(CORRECT_APPLICATION_NAME))));
        }
    }

}