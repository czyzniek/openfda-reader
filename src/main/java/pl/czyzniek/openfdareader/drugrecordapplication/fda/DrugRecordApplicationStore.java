package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.czyzniek.openfdareader.error.StructuredError;

interface DrugRecordApplicationStore {
    Either<StructuredError, ? extends Page<DrugRecordApplication>> findDrugRecordApplications(String manufacturerName, String brandName, Pageable page);
}
