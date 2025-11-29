package org.example.EmailFetcher.repository;

import org.example.EmailFetcher.model.EmailAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<EmailAttachmentEntity, Long> {
}
