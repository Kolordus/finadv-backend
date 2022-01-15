package pl.kolak.finansjera.financeEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface FinanceEntryRepository extends MongoRepository<FinanceEntry, String> {

    Optional<List<FinanceEntry>> findAllByPersonName(String name);
    Optional<FinanceEntry> findByDate(String date);
}
