package pl.kolak.finansjera.stuffrequest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface StuffRequestRepository extends MongoRepository<StuffRequest, String> {
    
    boolean deleteByDate(String date);
    Optional<StuffRequest> findByDateEquals(String date);
}
