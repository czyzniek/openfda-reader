package pl.czyzniek.openfdareader.drugrecordapplication.details;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.czyzniek.openfdareader.error.ErrorConfiguration;
import pl.czyzniek.openfdareader.error.NotFoundError;
import pl.czyzniek.openfdareader.error.ValidationError;

@WebMvcTest(controllers = {DrugRecordApplicationDetailController.class})
@Import({ErrorConfiguration.class})
class DrugRecordApplicationDetailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SaveDrugRecordApplicationDetailUseCase saveDrugRecordApplicationDetailUseCase;

    @MockBean
    FindDrugRecordApplicationDetailsUseCase findDrugRecordApplicationDetailsUseCase;

    @MockBean
    FindDrugRecordApplicationDetailByApplicationNumberUseCase findDrugRecordApplicationDetailByApplicationNumberUseCase;

    @Test
    void shouldCreateDrugRecordApplicationDetail() throws Exception {
        //given
        given(saveDrugRecordApplicationDetailUseCase.execute(any(SaveDrugRecordApplicationDetailUseCase.Input.class)))
            .willReturn(Either.right(null));

        //when
        final var result = this.mockMvc.perform(
            post("/drug-record-application-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"applicationNumber\": \"APPLICATION-NUMBER\",\n"
                    + "  \"manufacturerName\": \"MANUFACTURER-NAME\",\n"
                    + "  \"substanceName\": \"SUBSTANCE-NAME\",\n"
                    + "  \"productNumbers\": [\"Product1\", \"Product2\"]\n"
                    + "}"));

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldHandleDuplicatedDrugRecordApplicationDetails() throws Exception {
        //given
        given(saveDrugRecordApplicationDetailUseCase.execute(any(SaveDrugRecordApplicationDetailUseCase.Input.class)))
            .willReturn(Either.left(new ValidationError("Duplicated entities!")));

        //when
        final var result = this.mockMvc.perform(
            post("/drug-record-application-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"applicationNumber\": \"APPLICATION-NUMBER\",\n"
                    + "  \"manufacturerName\": \"MANUFACTURER-NAME\",\n"
                    + "  \"substanceName\": \"SUBSTANCE-NAME\",\n"
                    + "  \"productNumbers\": [\"Product1\", \"Product2\"]\n"
                    + "}"));

        //then
        result.andExpect(status().isBadRequest());

        //and
        result
            .andExpect(jsonPath("$.code", equalTo("VALIDATION_ERROR")))
            .andExpect(jsonPath("$.message", equalTo("Duplicated entities!")));
    }

    @Test
    void shouldFindDrugRecordApplicationByApplicationNumber() throws Exception {
        //given
        given(findDrugRecordApplicationDetailByApplicationNumberUseCase.execute(any(FindDrugRecordApplicationDetailByApplicationNumberUseCase.Input.class)))
            .willReturn(Either.right(new FindDrugRecordApplicationDetailByApplicationNumberUseCase.Output(
                DrugRecordApplicationDetailFixture.drugRecordApplicationDetail("APPLICATION-NUMBER"))));

        //when
        final var result = this.mockMvc.perform(
            get("/drug-record-application-details/APPLICATION-NUMBER"));

        //then
        result.andExpect(status().isOk());

        //and
        result
            .andExpect(jsonPath("$.applicationNumber", equalTo("APPLICATION-NUMBER")))
            .andExpect(jsonPath("$.manufacturerName", equalTo("MANUFACTURER_NAME")))
            .andExpect(jsonPath("$.substanceName", equalTo("SUBSTANCE_NAME")))
            .andExpect(jsonPath("$.productNumbers", hasSize(2)))
            .andExpect(jsonPath("$.productNumbers[0]", equalTo("PRODUCT1")))
            .andExpect(jsonPath("$.productNumbers[1]", equalTo("PRODUCT2")));
    }

    @Test
    void shouldHandleNotFindingDrugRecordApplicationDetail() throws Exception {
        //given
        given(findDrugRecordApplicationDetailByApplicationNumberUseCase.execute(any(FindDrugRecordApplicationDetailByApplicationNumberUseCase.Input.class)))
            .willReturn(Either.left(new NotFoundError("Could not find!")));

        //when
        final var result = this.mockMvc.perform(
            get("/drug-record-application-details/APPLICATION-NUMBER"));

        //then
        result.andExpect(status().isNotFound());

        //and
        result
            .andExpect(jsonPath("$.code", equalTo("NOT_FOUND_ERROR")))
            .andExpect(jsonPath("$.message", equalTo("Could not find!")));
    }
}