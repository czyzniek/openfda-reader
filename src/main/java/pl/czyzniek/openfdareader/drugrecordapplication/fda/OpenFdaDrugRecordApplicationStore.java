package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static org.springframework.util.StringUtils.hasText;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.czyzniek.openfdareader.error.NotFoundError;
import pl.czyzniek.openfdareader.error.ServerError;
import pl.czyzniek.openfdareader.error.StructuredError;
import pl.czyzniek.openfdareader.error.ValidationError;

@RequiredArgsConstructor
@Slf4j
class OpenFdaDrugRecordApplicationStore implements DrugRecordApplicationStore {
    private static final String DRUG_RECORD_APPLICATION_PATH = "/drug/drugsfda.json";
    private static final String SEARCH_PARAM = "search";
    private static final String SKIP_PARAM = "skip";
    private static final String LIMIT_PARAM = "limit";
    private static final String SORT_PARAM = "sort";

    private final RestTemplate restTemplate;
    private final String openFdaBaseUrl;

    @Override
    public Either<StructuredError, ? extends Page<DrugRecordApplication>> findDrugRecordApplications(String manufacturerName, String brandName, Pageable page) {
        final var searchParams = new SearchParams(manufacturerName, brandName);
        final var pageParams = PageParams.fromPage(page);
        final var url = UriComponentsBuilder
            .fromHttpUrl(openFdaBaseUrl)
            .path(DRUG_RECORD_APPLICATION_PATH)
            .queryParamIfPresent(SEARCH_PARAM, Optional.ofNullable(searchParams.toUrlParams()))
            .queryParamIfPresent(SKIP_PARAM, pageParams.map(PageParams::getSkip).toJavaOptional())
            .queryParamIfPresent(LIMIT_PARAM, pageParams.map(PageParams::getLimit).toJavaOptional())
            .queryParamIfPresent(SORT_PARAM, pageParams.flatMap(params -> Option.of(params.sort)).toJavaOptional())
            .build()
            .toUri();
        return Try.of(() -> restTemplate.getForObject(url, DrugResponse.class))
            .map(drugResponse -> new PageImpl<>(
                drugResponse.applications,
                PageRequest.of(drugResponse.meta.results.currentPage(), drugResponse.meta.results.limit),
                drugResponse.meta.results.total
            ))
            .onFailure(ex -> log.error("Could not fetch data from openFDA!", ex))
            .toEither()
            .mapLeft(this::handleError);
    }

    private StructuredError handleError(Throwable throwable) {
        return Match(throwable).of(
            Case($(instanceOf(HttpClientErrorException.class)), this::handleClientError),
            Case($(), ex -> new ServerError("Could not fetch data from openFDA!")));
    }

    private StructuredError handleClientError(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {
            case NOT_FOUND:
                return new NotFoundError("Could not find drug application records!");
            case BAD_REQUEST:
                return new ValidationError(ex.getMessage());
            default:
                return new ServerError(ex.getMessage());
        }
    }

    @Value
    private static class SearchParams {
        private static final String MANUFACTURER_NAME_PARAM = "patient.drug.openfda.manufacturer_name";
        private static final String BRAND_NAME_PARAM = "patient.drug.openfda.brand_name";

        String manufacturerName;
        String brandName;

        String toUrlParams() {
            final var paramsBuilder = new StringJoiner("+");
            if (hasText(manufacturerName)) {
                paramsBuilder.add(MANUFACTURER_NAME_PARAM + ":" + manufacturerName);
            }
            if (hasText(brandName)) {
                paramsBuilder.add(BRAND_NAME_PARAM + ":" + brandName);
            }
            return paramsBuilder.toString();
        }
    }

    @Value
    private static class PageParams {
        int limit;
        long skip;
        String sort;

        PageParams(Pageable pageable) {
            this.limit = pageable.getPageSize();
            this.skip = pageable.getOffset();
            this.sort = mapSort(pageable.getSort());
        }

        static Option<PageParams> fromPage(Pageable page) {
            return Option.of(page)
                .filter(Pageable::isPaged)
                .map(PageParams::new);
        }

        private static String mapSort(Sort pageSort) {
            return pageSort.get()
                .map(PageParams::mapOrder)
                .findFirst()
                .orElse(null);
        }

        private static String mapOrder(Sort.Order order) {
            return order.toString().replaceAll("\\s", "");
        }
    }

    @Value
    private static class DrugResponse {
        Meta meta;
        @JsonProperty("results")
        List<DrugRecordApplication> applications;

        @Value
        static class Meta {
            String disclaimer;
            String terms;
            String license;
            String lastUpdated;
            Result results;

            @Value
            static class Result {
                int skip;
                int limit;
                int total;

                int currentPage() {
                    return skip / limit;
                }
            }
        }
    }
}
