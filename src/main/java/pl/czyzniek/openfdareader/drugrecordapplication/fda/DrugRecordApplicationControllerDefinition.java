package pl.czyzniek.openfdareader.drugrecordapplication.fda;

import java.util.List;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface DrugRecordApplicationControllerDefinition {

    @GetMapping("/drug-record-applications/fda")
    List<DrugRecordApplication> findFdaDrugRecordApplications(@RequestParam(required = false) String manufacturerName,
                                                              @RequestParam(required = false) String brandName,
                                                              @ParameterObject @RequestParam(required = false) @PageableDefault Pageable pageable);
}
