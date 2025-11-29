package org.example.EmailFetcher.repository;

import org.example.EmailFetcher.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByReceiverEmail(String receiverEmail);
}
