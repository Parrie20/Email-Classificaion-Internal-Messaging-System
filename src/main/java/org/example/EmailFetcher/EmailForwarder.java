package org.example.EmailFetcher;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.EmailFetcher.model.CategorizedEmailEntity;
import org.example.EmailFetcher.model.DepartmentHeadEntity;
import org.example.EmailFetcher.repository.CategorizedEmailRepository;
import org.example.EmailFetcher.repository.DepartmentHeadRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class EmailForwarder {
    private final CategorizedEmailRepository categorizedEmailRepository;
    private final DepartmentHeadRepository departmentHeadRepository;

    public EmailForwarder(CategorizedEmailRepository categorizedEmailRepository,
                          DepartmentHeadRepository departmentHeadRepository) {
        this.categorizedEmailRepository = categorizedEmailRepository;
        this.departmentHeadRepository = departmentHeadRepository;
    }

    // ✅ Automatically forward new categorized emails every 5 minutes
    @Scheduled(fixedRate = 300000)
    @Transactional  // Ensures database consistency
    public void forwardCategorizedEmails() {
        System.out.println("📧 Checking for new categorized emails to forward...");

        List<CategorizedEmailEntity> emails = categorizedEmailRepository.findByForwardedFalse();
        if (emails.isEmpty()) {
            System.out.println("✅ No new categorized emails to forward.");
            return;
        }

        for (CategorizedEmailEntity email : emails) {
            Optional<DepartmentHeadEntity> departmentHeadOpt =
                    departmentHeadRepository.findByDepartmentIgnoreCase(email.getCategory());

            if (departmentHeadOpt.isEmpty()) {
                System.err.println("⚠ No department head found for category: " + email.getCategory());
                continue;
            }

            DepartmentHeadEntity departmentHead = departmentHeadOpt.get();

            // ✅ Mark email as forwarded BEFORE sending to avoid duplication
            email.setForwarded(true);
            categorizedEmailRepository.save(email);

            boolean success = sendEmail(email, departmentHead);

            if (success) {
                System.out.println("✅ Email successfully forwarded to: " + departmentHead.getEmail());
            } else {
                // ❌ If email sending fails, reset the `forwarded` flag to `false` so it gets retried
                email.setForwarded(false);
                categorizedEmailRepository.save(email);
                System.err.println("⚠ Failed to forward email to " + departmentHead.getEmail() + ". Will retry later.");
            }
        }
    }

    // ✅ Send email using department head's SMTP credentials
    private boolean sendEmail(CategorizedEmailEntity email, DepartmentHeadEntity departmentHead) {
        try {
            Properties props = getSmtpProperties();

            final String senderEmail = "testerid0320@gmail.com";  // ✅ Company email
            final String senderPassword = "gmrf fqkf wirl tvfn"; // ✅ Use App Password

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail)); // ✅ Company email as sender
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(departmentHead.getEmail()));
            message.setSubject("📩 Forwarded Email: " + email.getSubject());
            message.setText("From: " + email.getSender() + "\n\n" + email.getContent());

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Extract SMTP properties into a separate method for reuse
    private Properties getSmtpProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }
}
