package org.example.EmailFetcher;

import jakarta.mail.internet.MimeMessage;
import org.example.EmailFetcher.model.CategorizedEmailEntity;
import org.example.EmailFetcher.model.EmailEntity;
import org.example.EmailFetcher.model.EmailAttachmentEntity;
import org.example.EmailFetcher.repository.CategorizedEmailRepository;
import org.example.EmailFetcher.repository.EmailRepository;
import org.example.EmailFetcher.repository.AttachmentRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.google.api.client.auth.oauth2.Credential;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailFetcher {
    private final EmailRepository emailRepository;
    private final AttachmentRepository attachmentRepository;
    private final CategorizedEmailRepository categorizedEmailRepository; // Add repository for categorized emails
    private final OAuth oAuthService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gmail.user.email}")
    private String userEmail;

    @Value("${gmail.imap.host}")
    private String imapHost;

    @Value("${gmail.imap.port}")
    private String imapPort;

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    public EmailFetcher(EmailRepository emailRepository, AttachmentRepository attachmentRepository,
                        CategorizedEmailRepository categorizedEmailRepository, OAuth oAuthService) {
        this.emailRepository = emailRepository;
        this.attachmentRepository = attachmentRepository;
        this.categorizedEmailRepository = categorizedEmailRepository;  // Inject categorized email repository
        this.oAuthService = oAuthService;
    }

    public void fetchEmails() {
        try {
            Credential credential = oAuthService.getCredentials();
            String accessToken = credential.getAccessToken();

            if (accessToken == null) {
                throw new IllegalStateException("Access token is null. Ensure OAuth authentication is successful.");
            }

            // IMAP properties
            Properties props = new Properties();
            props.put("mail.imap.ssl.enable", "true");
            props.put("mail.imap.auth.mechanisms", "XOAUTH2");

            Session session = Session.getInstance(props);
            try (Store store = session.getStore("imap")) {
                store.connect(imapHost, userEmail, accessToken);

                try (Folder inbox = store.getFolder("INBOX")) {
                    inbox.open(Folder.READ_ONLY);
                    for (Message message : inbox.getMessages()) {
                        processEmail(message);
                    }
                }
            }
        } catch (AuthenticationFailedException e) {
            System.err.println("OAuth Authentication failed. Possible expired token: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processEmail(Message message) throws Exception {
        String emailId = ((MimeMessage) message).getMessageID();
        Address[] fromAddresses = message.getFrom();
        String sender = (fromAddresses != null && fromAddresses.length > 0) ? fromAddresses[0].toString() : "Unknown Sender";

        if (!emailRepository.existsByEmailId(emailId) && sender != null && !sender.isEmpty()) {
            EmailEntity email = new EmailEntity();
            email.setEmailId(emailId);
            email.setSender(sender);
            email.setSubject(message.getSubject());
            email.setreceivedAt(Timestamp.from(Instant.now()));

            String emailContent = getTextFromMessage(message);
            email.setContent(emailContent);

            // Save the email to emails schema (EmailEntity)
            emailRepository.save(email);

            // 🛠 CALL FLASK API TO GET EMAIL CATEGORY
            String category = categorizeEmail(emailContent);

            // Save the categorized email in categorized_emails schema
            CategorizedEmailEntity categorizedEmail = new CategorizedEmailEntity(sender, message.getSubject(), emailContent, category);
            categorizedEmailRepository.save(categorizedEmail);

            // Extract and save attachments
            List<EmailAttachmentEntity> attachments = getAttachmentsFromMessage(message, email);
            if (!attachments.isEmpty()) {
                attachmentRepository.saveAll(attachments);
            }
        }
    }

    private String categorizeEmail(String content) {
        try {
            Map<String, String> requestBody = Map.of("content", content);
            ResponseEntity<Map> response = restTemplate.exchange(
                    flaskApiUrl, HttpMethod.POST, new HttpEntity<>(requestBody, new HttpHeaders()), Map.class
            );

            String category = response.getBody().get("category").toString();
            System.out.println("Category received from Flask API: " + category); // Debugging log
            return category;
        } catch (Exception e) {
            System.err.println("Error calling Flask API: " + e.getMessage());
            return "Unknown"; // Default category in case of API failure
        }
    }


    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(multipart);
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart multipart) throws Exception {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                result.append(part.getContent().toString());
            }
        }
        return result.toString();
    }

    private List<EmailAttachmentEntity> getAttachmentsFromMessage(Message message, EmailEntity email) throws Exception {
        List<EmailAttachmentEntity> attachments = new ArrayList<>();

        if (message.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String filename = part.getFileName();
                    try (InputStream inputStream = part.getInputStream();
                         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        attachments.add(new EmailAttachmentEntity(filename, outputStream.toByteArray(), email));
                    }
                }
            }
        }
        return attachments;
    }

    @Scheduled(fixedRate = 60000) // Fetch new emails every 1 min
    public void fetchNewEmails() {
        fetchEmails();
    }
}
