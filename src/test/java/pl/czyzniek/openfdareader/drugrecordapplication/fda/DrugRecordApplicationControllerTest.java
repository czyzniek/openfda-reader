package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.czyzniek.openfdareader.drugrecordapplication.fda.DrugRecordApplicationFixture.drugRecordApplication;

import io.vavr.control.Either;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import pl.czyzniek.openfdareader.error.ErrorConfiguration;
import pl.czyzniek.openfdareader.error.ServerError;

@WebMvcTest(controllers = {DrugRecordApplicationController.class})
@Import({ErrorConfiguration.class})
class DrugRecordApplicationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FindFdaDrugRecordApplicationsUseCase findFdaDrugRecordApplicationsUseCase;

    @Test
    void shouldMapResponseFromUseCaseToJson() throws Exception {
        //given
        given(findFdaDrugRecordApplicationsUseCase.execute(any(FindFdaDrugRecordApplicationsUseCase.Input.class)))
            .willReturn(Either.right(new FindFdaDrugRecordApplicationsUseCase.Output(List.of(drugRecordApplication("TEST-APPLICATION-NUMBER")))));

        //when
        final var result = this.mockMvc.perform(
            get("/drug-record-applications/fda")
                .param("manufacturerName", "MANUFACTURER"));

        //then
        result.andExpect(status().isOk());

        //and
        result
            .andExpect(jsonPath("$.size()", equalTo(1)))
            .andExpect(jsonPath("$[0].applicationNumber", equalTo("TEST-APPLICATION-NUMBER")))
            .andExpect(jsonPath("$[0].sponsorName", equalTo("SPONSOR_NAME")))
            .andExpect(jsonPath("$[0].submissions.size()", equalTo(1)))
            .andExpect(jsonPath("$[0].submissions[0].type", equalTo("SUPPL")))
            .andExpect(jsonPath("$[0].submissions[0].number", equalTo("5")))
            .andExpect(jsonPath("$[0].submissions[0].status", equalTo("AP")))
            .andExpect(jsonPath("$[0].submissions[0].statusDate", equalTo("1988-01-22")))
            .andExpect(jsonPath("$[0].submissions[0].classCode", equalTo("MANUF (CA)")))
            .andExpect(jsonPath("$[0].submissions[0].classCodeDescription", equalTo("Manufacturing (CMC)")))
            .andExpect(jsonPath("$[0].submissions[0].applicationDocs.size()", equalTo(0)))
            .andExpect(jsonPath("$[0].products.size()", equalTo(1)))
            .andExpect(jsonPath("$[0].products[0].number", equalTo("001")))
            .andExpect(jsonPath("$[0].products[0].referenceDrug", equalTo("No")))
            .andExpect(jsonPath("$[0].products[0].brandName", equalTo("ALLOPURINOL")))
            .andExpect(jsonPath("$[0].products[0].activeIngredients.size()", equalTo(1)))
            .andExpect(jsonPath("$[0].products[0].activeIngredients[0].name", equalTo("ALLOPURINOL")))
            .andExpect(jsonPath("$[0].products[0].activeIngredients[0].strength", equalTo("100MG")))
            .andExpect(jsonPath("$[0].products[0].referenceStandard", equalTo("No")))
            .andExpect(jsonPath("$[0].products[0].dosageForm", equalTo("TABLET")))
            .andExpect(jsonPath("$[0].products[0].route", equalTo("ORAL")))
            .andExpect(jsonPath("$[0].products[0].marketingStatus", equalTo("Prescription")));
    }

    @Test
    void shouldHandleErrorsFromUseCase() throws Exception {
        //given
        given(findFdaDrugRecordApplicationsUseCase.execute(any(FindFdaDrugRecordApplicationsUseCase.Input.class)))
            .willReturn(Either.left(new ServerError("Could not find drugs!")));

        //when
        final var result = this.mockMvc.perform(
            get("/drug-record-applications/fda")
                .param("manufacturerName", "MANUFACTURER"));

        //then
        result.andExpect(status().isInternalServerError());

        //and
        result
            .andExpect(jsonPath("$.code", equalTo("SERVER_ERROR")))
            .andExpect(jsonPath("$.message", equalTo("Could not find drugs!")));
    }

}