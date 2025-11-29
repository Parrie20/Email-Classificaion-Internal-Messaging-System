package org.example.EmailFetcher.model;

import jakarta.persistence.*;
import org.example.EmailFetcher.model.EmailAttachmentEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emails", uniqueConstraints = {@UniqueConstraint(columnNames = "email_id")})
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_id", nullable = false, unique = true)
    private String emailId;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "subject")
    private String subject;

    @Column(name = "received_at", nullable = false)
    private Timestamp receivedAt;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmailAttachmentEntity> attachments;

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Timestamp getreceivedAt() { return receivedAt; }
    public void setreceivedAt(Timestamp receivedDate) { this.receivedAt = receivedDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<EmailAttachmentEntity> getAttachments() { return attachments; }
    public void setAttachments(List<EmailAttachmentEntity> attachments) { this.attachments = attachments; }
}
