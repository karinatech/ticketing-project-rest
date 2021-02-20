package com.cybertek.repository;

import com.cybertek.entity.ConfirmationTokn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTookenRepo extends JpaRepository<ConfirmationTokn,Integer> {
    Optional<ConfirmationTokn>findByToken(String token);

}
