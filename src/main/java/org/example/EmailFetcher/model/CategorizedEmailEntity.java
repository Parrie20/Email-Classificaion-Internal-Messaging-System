package org.example.EmailFetcher.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorized_emails")
public class CategorizedEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String category;

    // ✅ New Field: Track if email is forwarded
    private boolean forwarded = false;

    // ✅ Add No-Args Constructor (Mandatory for JPA)
    public CategorizedEmailEntity() {
    }

    // ✅ Add All-Args Constructor
    public CategorizedEmailEntity(String sender, String subject, String content, String category) {
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.category = category;
        this.forwarded = false;
    }

    // ✅ Add Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // ✅ Getter and Setter for "forwarded" field
    public boolean isForwarded() {
        return forwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }
}
