package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface DrugRecordApplicationDetailRepository extends CrudRepository<DrugRecordApplicationDetail, String>, WithInsert {

    @Query(
        "SELECT * FROM drug_record_application_detail d "
            + "WHERE d.manufacturer_name = :manufacturerName "
            + "OR d.substance_name = :substanceName "
            + "OR d.product_numbers LIKE CONCAT('%', :productNumbers, '%')")
    List<DrugRecordApplicationDetail> findAllDrugRecordApplicationDetail(@Param("manufacturerName") String manufacturerName,
                                                                         @Param("substanceName") String substanceName,
                                                                         @Param("productNumbers") String productNumbers);
}
