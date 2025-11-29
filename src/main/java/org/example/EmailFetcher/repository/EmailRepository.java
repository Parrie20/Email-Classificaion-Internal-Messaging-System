package org.example.EmailFetcher.repository;

import org.example.EmailFetcher.model.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    @Query("SELECT MAX(e.receivedAt) FROM EmailEntity e")
    Timestamp findLatestreceivedAt();
    boolean existsByEmailId(String emailId);
    @Query("SELECT e FROM EmailEntity e WHERE e.id NOT IN (SELECT c.id FROM CategorizedEmailEntity c)")
    List<EmailEntity> findUncategorizedEmails();
}
