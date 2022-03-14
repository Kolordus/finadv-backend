package pl.kolak.finansjera.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kolak.finansjera.stuffrequest.StuffRequest;
import pl.kolak.finansjera.stuffrequest.StuffRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requests")
public class StuffRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(StuffRequestController.class);
    private final StuffRequestService service;

    @Autowired
    public StuffRequestController(StuffRequestService stuffRequestService) {
        this.service = stuffRequestService;
    }

    @GetMapping
    public ResponseEntity<List<StuffRequest>> readAllStuffRequests(HttpServletRequest request) {
        List<StuffRequest> stuffRequests = service.readAllStuffRequests();
        LOG.info("From: {} || returned entries amount: {}", request.getRemoteAddr(), stuffRequests.size());

        return new ResponseEntity<>(stuffRequests, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createStuffRequest(
            @Valid @RequestBody StuffRequest request,
            HttpServletRequest httpReq) {
        service.createStuffRequest(request);

        LOG.info("From: {} || saved entity: {}", httpReq.getRemoteAddr(), request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStuffRequest(@RequestParam String date) {
        return service.findByDate(date)
                .map(request -> {
                    service.deleteStuffRequestByDate(date);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
