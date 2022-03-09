package pl.kolak.finansjera.finance_entity;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.kolak.finansjera.utils.InvalidDataException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceDataService {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceDataService.class);

    private final FinanceEntryRepository financeEntryRepository;

    public FinanceDataService(FinanceEntryRepository financeEntryRepository) {
        this.financeEntryRepository = financeEntryRepository;
    }

    public List<FinanceEntry> getAllFinanceEntries() {
        return financeEntryRepository.findAll();
    }

    public void createFinanceEntry(FinanceEntry entry) {
        entry.setPersonName(entry.getPersonName().toLowerCase());

        financeEntryRepository.save(entry);
    }

    public void updateEntry(FinanceEntry newEntry) {
        financeEntryRepository.findByDateEquals(newEntry.getDate()).ifPresentOrElse(entry -> {
            LOG.info("entry before update: {}", entry);
            entry.updateEntryValues(newEntry);
            financeEntryRepository.save(entry);
            LOG.info("entry after update: {}", entry);

        }, () -> {
            throw new InvalidDataException("no such entry");
        });
    }

    public void clearEntries() {
        financeEntryRepository.deleteAll();
    }

    public List<FinanceEntry> readAllFinanceEntriesByName(String name) {
        return financeEntryRepository.findAllByPersonName(name)
                .orElse(Collections.emptyList());
    }

    // by date because flutter doesnt store data about entry id and most unique is date
    public void deleteEntryOrThrow(FinanceEntry entry) {
        Optional<FinanceEntry> byDate = financeEntryRepository.findByDateEquals(entry.getDate());

        if (byDate.isPresent())
            financeEntryRepository.delete(entry);
        else
            throw new InvalidDataException("Couldn't find entry with such date: " + entry.getDate());
    }
}
