package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
class FdaDrugRecordApplicationConfiguration {

    @Bean
    DrugRecordApplicationStore drugRecordApplicationStore(RestTemplate restTemplate,
                                                          @Value("${fda.baseUrl}") String fdaBaseUrl) {
        return new OpenFdaDrugRecordApplicationStore(restTemplate, fdaBaseUrl);
    }

    @Bean
    FindFdaDrugRecordApplicationsUseCase findFdaDrugRecordApplicationsUseCase(DrugRecordApplicationStore drugRecordApplicationStore) {
        return new FindFdaDrugRecordApplicationsUseCase(drugRecordApplicationStore);
    }

    @Bean
    RestTemplate restTemplate(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder, RestTemplateBuilder restTemplateBuilder) {
        final var snakeCaseObjectMapper = jackson2ObjectMapperBuilder
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();
        return restTemplateBuilder
            .messageConverters(List.of(new MappingJackson2HttpMessageConverter(snakeCaseObjectMapper)))
            .build();
    }
}
