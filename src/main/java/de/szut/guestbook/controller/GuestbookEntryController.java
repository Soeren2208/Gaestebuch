package de.szut.guestbook.controller;

import de.szut.guestbook.model.GuestbookEntry;
import de.szut.guestbook.repository.GuestbookEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/guestbook")
public class GuestbookEntryController {
    @Autowired
    private GuestbookEntryRepository repository;

    /*
    @RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces =  MediaType.APPLICATION_JSON_VALUE)
    */
    @PostMapping
    public ResponseEntity<GuestbookEntry> createGuestbookEntry(@RequestBody GuestbookEntry entry){
        entry = repository.save(entry);
        return new ResponseEntity(entry, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestbookEntry> findGuestbookEntryById(@PathVariable Long id){
        Optional<GuestbookEntry> entry = repository.findById(id);
        if(entry.isPresent()){
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<GuestbookEntry>> findAllGuestbookEntries(@RequestParam(required = false) Integer year){
        if(year == null){
            return ResponseEntity.ok(repository.findAllByOrderByDateDesc());
        }
        else{
            return ResponseEntity.ok(this.repository.findAllByDateBetweenOrderByDateDesc(LocalDate.of(year, 1,1),
                    LocalDate.of(year, 12,31)));
        }
    }

/*
    @GetMapping
    public ResponseEntity<List<GuestbookEntry>> findAllGuestbookEntriesByYear(@RequestParam Integer year){
        List<GuestbookEntry> response = this.repository.findAllByOrderByDateDesc()
                    .stream()
                    .filter(entry -> entry.getDate().getYear() == year)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
*/

    @PutMapping("/{id}")
    public ResponseEntity<GuestbookEntry> updateGuestbookEntry(@PathVariable Long id, @RequestBody GuestbookEntry entryToUpdate){
        Optional<GuestbookEntry> response = repository.findById(id)
                .map(entry -> {
                    entry.setTitle(entryToUpdate.getTitle());
                    entry.setComment(entryToUpdate.getComment());
                    entry.setCommenter(entryToUpdate.getCommenter());
                    return repository.save(entry);
                });
        if(response.isPresent()){
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void deleteGuestbookEntryById(@PathVariable Long id) {
        repository.deleteById(id);
    }
}