package pl.kolak.finansjera.balance;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BalanceRepository extends MongoRepository<Balance, String> {}
