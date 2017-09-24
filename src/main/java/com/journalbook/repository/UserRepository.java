package com.journalbook.repository;

import com.journalbook.entity.User_;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User_, Long> {

    User_ findByEmailAddress(String email);
    
    User_ findByToken(String token);
}
