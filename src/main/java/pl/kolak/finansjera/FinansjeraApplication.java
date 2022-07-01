package pl.kolak.finansjera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.kolak.finansjera.stuffrequest.StuffRequest;
import pl.kolak.finansjera.stuffrequest.StuffRequestService;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class FinansjeraApplication {

    @Autowired
    private StuffRequestService stuffRequestService;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(FinansjeraApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void bootstrap() {
//        stuffRequestService.createStuffRequest(new StuffRequest("2020-12-20", "Pau", "cos tam"));
//        stuffRequestService.createStuffRequest(new StuffRequest("2021-05-24", "Jack", "cos innego"));
//        stuffRequestService.createStuffRequest(new StuffRequest("2022-01-04", "Pau", "cos kolejnego"));
//    }

    static class Observable {
        Set<Observators> observatorsSet = new HashSet<>();

        public void subscribe(Observators observators) {
            this.observatorsSet.add(observators);
        }

        public void notifyAllObs(String wiadomosc) {
            for (Observators observators : observatorsSet) {
                observators.notifyng(wiadomosc);
            }
        }
    }

    static class Observators {

        public static int numerek = 0;
        private final int przypisany;


        public Observators() {
            this.przypisany = Observators.numerek++;
        }

        public void notifyng(String event) {
            System.out.println("heloo from " + this.przypisany + " - " + event);
        }
    }
}
