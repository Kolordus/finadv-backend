package pl.kolak.finansjera.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kolak.finansjera.stuffrequest.StuffRequest;
import pl.kolak.finansjera.stuffrequest.StuffRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requests")
public class StuffRequestController {
    
    private final StuffRequestService service;

    // todo logger!!

    @Autowired
    public StuffRequestController(StuffRequestService stuffRequestService) {
        this.service = stuffRequestService;
    }
    
    @PostMapping
    public ResponseEntity<?> createStuffRequest(@Valid @RequestBody StuffRequest request) {
        System.out.println("halo halo");
        service.createStuffRequest(request);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @GetMapping
    public ResponseEntity<List<StuffRequest>> readAllStuffRequests() {
        return new ResponseEntity<>(
                service.readAllStuffReuqest(),
                HttpStatus.OK);
    }
    
    @PutMapping
    public ResponseEntity<?> updateStaffRequest(@RequestBody StuffRequest request) {
        service.updateStuffRequest(request);
        
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @DeleteMapping
    public ResponseEntity<?> deleteStuffRequest(@RequestBody String date) {
        return service.deleteStuffRequestByDate(date) ?
                new ResponseEntity<>(HttpStatus.ACCEPTED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
