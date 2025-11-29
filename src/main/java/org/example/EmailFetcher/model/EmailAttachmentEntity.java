package org.example.EmailFetcher.model;
import jakarta.persistence.*;
import org.example.EmailFetcher.model.EmailEntity;

@Entity
@Table(name = "attachments")
public class EmailAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "email_id", nullable = false)
    private EmailEntity email;

    @Column(name = "filename")
    private String filename;

    @Lob
    @Column(name = "file_data", columnDefinition = "BYTEA")
    private byte[] fileData;

    public EmailAttachmentEntity() {}

    public EmailAttachmentEntity(String filename, byte[] fileData, EmailEntity email) {
        this.filename = filename;
        this.fileData = fileData;
        this.email = email;
    }

    // Getters and Setters
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public EmailEntity getEmail() { return email; }
    public void setEmail(EmailEntity email) { this.email = email; }
}