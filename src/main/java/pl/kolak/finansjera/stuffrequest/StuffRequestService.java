package pl.kolak.finansjera.stuffrequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StuffRequestService {

    private final StuffRequestRepository repository;

    @Autowired
    public StuffRequestService(StuffRequestRepository stuffRequestRepository) {
        this.repository = stuffRequestRepository;
    }

    public void createStuffRequest(StuffRequest request) {
        repository.save(request);
    }

    public List<StuffRequest> readAllStuffRequests() {
        return repository.findAll();
    }
        
    public void deleteStuffRequestByDate(String date) {
        repository.deleteByDate(date);
    }

    public Optional<StuffRequest> findByDate(String date) {
        return repository.findByDateEquals(date);
    }
}
