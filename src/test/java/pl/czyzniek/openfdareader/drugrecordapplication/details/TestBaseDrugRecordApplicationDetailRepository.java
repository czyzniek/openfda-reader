package pl.czyzniek.openfdareader.drugrecordapplication.details;

import java.util.List;
import java.util.Optional;

class TestBaseDrugRecordApplicationDetailRepository implements DrugRecordApplicationDetailRepository {

    @Override
    public DrugRecordApplicationDetail insert(DrugRecordApplicationDetail detail) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public <S extends DrugRecordApplicationDetail> S save(S entity) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public <S extends DrugRecordApplicationDetail> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public Optional<DrugRecordApplicationDetail> findById(String s) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public Iterable<DrugRecordApplicationDetail> findAll() {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public Iterable<DrugRecordApplicationDetail> findAllById(Iterable<String> strings) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public void delete(DrugRecordApplicationDetail entity) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public void deleteAll(Iterable<? extends DrugRecordApplicationDetail> entities) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }

    @Override
    public List<DrugRecordApplicationDetail> findAllDrugRecordApplicationDetail(String manufacturerName,
                                                                                String substanceName,
                                                                                String productNumbers) {
        throw new UnsupportedOperationException("Should noe be used in this scenario!");
    }
}
