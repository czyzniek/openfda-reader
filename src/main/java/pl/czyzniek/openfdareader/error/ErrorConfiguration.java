package pl.czyzniek.openfdareader.error;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfiguration {

    @Bean
    StructuredErrorExceptionHandler structuredErrorExceptionHandler() {
        return new StructuredErrorExceptionHandler();
    }
}
