package de.szut.guestbook.repository;

import de.szut.guestbook.model.GuestbookEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GuestbookEntryRepository extends JpaRepository<GuestbookEntry, Long> {
    public List<GuestbookEntry> findAllByOrderByDateDesc();
    public List<GuestbookEntry> findAllByDateBetweenOrderByDateDesc(LocalDate first, LocalDate second);
}