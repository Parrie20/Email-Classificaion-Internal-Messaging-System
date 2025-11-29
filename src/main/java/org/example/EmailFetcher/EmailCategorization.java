package org.example.EmailFetcher;

import org.example.EmailFetcher.model.CategorizedEmailEntity;
import org.example.EmailFetcher.model.EmailEntity;
import org.example.EmailFetcher.repository.CategorizedEmailRepository;
import org.example.EmailFetcher.repository.EmailRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailCategorization {
    private final EmailRepository emailRepository;
    private final CategorizedEmailRepository categorizedEmailRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    public EmailCategorization(EmailRepository emailRepository, CategorizedEmailRepository categorizedEmailRepository) {
        this.emailRepository = emailRepository;
        this.categorizedEmailRepository = categorizedEmailRepository;
    }

    public void categorizeAndStoreEmails() {
        List<EmailEntity> uncategorizedEmails = emailRepository.findAll(); // Fetch all emails

        for (EmailEntity email : uncategorizedEmails) {
            // ✅ Check if the email is already categorized
            Optional<CategorizedEmailEntity> existingEmail = categorizedEmailRepository.findBySenderAndSubject(email.getSender(), email.getSubject());

            if (existingEmail.isPresent()) {
                System.out.println("⚠ Email already categorized: " + email.getSubject() + " (Skipping...)");
                continue; // Skip categorization if already exists
            }

            // Call Flask API for categorization
            String category = categorizeEmail(email.getContent());

            // Create and save categorized email
            CategorizedEmailEntity categorizedEmail = new CategorizedEmailEntity(
                    email.getSender(), email.getSubject(), email.getContent(), category
            );

            categorizedEmailRepository.save(categorizedEmail);
            System.out.println("✅ Saved categorized email: " + email.getSender() + " → " + category);
        }
    }

    private String categorizeEmail(String content) {
        try {
            Map<String, String> requestBody = Map.of("content", content);
            ResponseEntity<Map> response = restTemplate.exchange(
                    flaskApiUrl, HttpMethod.POST, new HttpEntity<>(requestBody, new HttpHeaders()), Map.class
            );

            return response.getBody().get("category").toString();
        } catch (Exception e) {
            System.err.println("❌ Error calling Flask API: " + e.getMessage());
            return "Unknown"; // Fallback category
        }
    }
}
