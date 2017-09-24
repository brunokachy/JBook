package com.journalbook.repository;

import com.journalbook.entity.Entry;
import com.journalbook.entity.User_;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByUser_(User_ user);
    
    List<Entry> findByEntryDate(Date entryDate);
    
    List<Entry> findByEntryDateBetween(Date today, Date entryDate);
}
