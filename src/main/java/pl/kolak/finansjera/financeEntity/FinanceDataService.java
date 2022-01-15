package pl.kolak.finansjera.financeEntity;


import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.kolak.finansjera.utils.InvalidDataException;

import java.util.*;

@Service
public class FinanceDataService {

    private final FinanceEntryRepository financeEntryRepository;
    private static final List<String> whiteList = List.of("pau", "jack");

    public FinanceDataService(FinanceEntryRepository financeEntryRepository) {
        this.financeEntryRepository = financeEntryRepository;
    }

    public List<FinanceEntry> getAllFinanceEntries() {
        return financeEntryRepository.findAll();
    }

    public void saveFinanceEntry(FinanceEntry entry) {
        entry.setPersonName(entry.getPersonName().toLowerCase());

        financeEntryRepository.save(entry);
    }

    public void validateData(FinanceEntry entry) {
        if (StringUtils.isBlank(entry.getPersonName()) || !whiteList.contains(entry.getPersonName().toLowerCase()))
            throw new InvalidDataException("Person name not accepted or is empty!");
        if (StringUtils.isBlank(entry.getName()))
            throw new InvalidDataException("Name is empty!");
        if (StringUtils.isBlank(entry.getDate()))
            throw new InvalidDataException("No date given");
        if (entry.getAmount() <= 0)
            throw new InvalidDataException("Amount must be positive");
    }

    public void updateFinanceEntry(FinanceEntry entry) {
        financeEntryRepository.findByDate(entry.getDate()).ifPresent(financeEntry -> {
            financeEntry.setAmount(entry.getAmount());
            financeEntry.setDate(entry.getDate());
            financeEntry.setName(entry.getName());
        });
    }

    public void clearEntries() {
        financeEntryRepository.deleteAll();
    }

    public List<FinanceEntry> getAllFinanceEntriesByName(String name) {
        return financeEntryRepository.findAllByPersonName(name)
                .orElse(Collections.emptyList());
    }

    // by date because flutter doesnt store data about entry id and most unique is date
    public void deleteEntryOrThrow(FinanceEntry entry) {
        Optional<FinanceEntry> byDate = financeEntryRepository.findByDate(entry.getDate());

        if (byDate.isPresent())
            financeEntryRepository.delete(entry);
        else
            throw new InvalidDataException("Couldn't find entry with such date: " + entry.getDate());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void method() {

    }

}
