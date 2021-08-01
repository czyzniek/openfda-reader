package pl.czyzniek.openfdareader.drugrecordapplication.details;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

@RequiredArgsConstructor
class WithInsertImpl implements WithInsert {
    private final JdbcAggregateTemplate template;

    @Override
    public DrugRecordApplicationDetail insert(DrugRecordApplicationDetail detail) {
        return template.insert(detail);
    }
}
