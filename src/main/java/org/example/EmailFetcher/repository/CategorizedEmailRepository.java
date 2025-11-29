package org.example.EmailFetcher.repository;

import org.example.EmailFetcher.model.CategorizedEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorizedEmailRepository extends JpaRepository<CategorizedEmailEntity, Long> {
    List<CategorizedEmailEntity> findByForwardedFalse();
    Optional<CategorizedEmailEntity> findBySenderAndSubject(String sender, String subject);
}
