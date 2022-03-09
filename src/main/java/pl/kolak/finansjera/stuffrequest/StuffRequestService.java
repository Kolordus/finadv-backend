package pl.kolak.finansjera.stuffrequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<StuffRequest> readAllStuffReuqest() {
        return repository.findAll();
    }
    
    public void updateStuffRequest(StuffRequest request) {
        
//        repository.findByDateEquals(request.getDate()).ifPresentOrElse(reqToUpdate -> {
//            // TODO logger - jak wyglądało przedtem
//            reqToUpdate.setUpdatedValues(request);
//            repository.save(reqToUpdate);
//        });
//
//        return ;
    }
    
    public boolean deleteStuffRequestByDate(String date) {
        return repository.deleteByDate(date);
    }
}
