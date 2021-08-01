package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories
class DrugRecordApplicationDetailsConfiguration extends AbstractJdbcConfiguration {

    @Bean
    SaveDrugRecordApplicationDetailUseCase saveDrugRecordApplicationDetailUseCase(DrugRecordApplicationDetailRepository repository) {
        return new SaveDrugRecordApplicationDetailUseCase(repository);
    }

    @Bean
    FindDrugRecordApplicationDetailsUseCase findDrugRecordApplicationDetailsUseCase(DrugRecordApplicationDetailRepository repository) {
        return new FindDrugRecordApplicationDetailsUseCase(repository);
    }

    @Bean
    FindDrugRecordApplicationDetailByApplicationNumberUseCase findDrugRecordApplicationDetailByApplicationNumberUseCase(
        DrugRecordApplicationDetailRepository repository) {
        return new FindDrugRecordApplicationDetailByApplicationNumberUseCase(repository);
    }

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
            StringToProductNumbersConverter.INSTANCE,
            ProductNumbersToStringConverter.INSTANCE
        ));
    }

    @ReadingConverter
    enum StringToProductNumbersConverter implements Converter<String, DrugRecordApplicationDetail.ProductNumbers> {
        INSTANCE;

        @Override
        public DrugRecordApplicationDetail.ProductNumbers convert(String source) {
            return new DrugRecordApplicationDetail.ProductNumbers(List.of(source.split(",")));
        }
    }

    @WritingConverter
    enum ProductNumbersToStringConverter implements Converter<DrugRecordApplicationDetail.ProductNumbers, String> {
        INSTANCE;

        @Override
        public String convert(DrugRecordApplicationDetail.ProductNumbers source) {
            return String.join(",", source.getValues());
        }
    }
}
