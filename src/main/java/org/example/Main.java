package org.example;

import org.example.EmailFetcher.DepartmentHeadService;
import org.example.EmailFetcher.EmailCategorization;
import org.example.EmailFetcher.EmailFetcher;
import org.example.EmailFetcher.EmailForwarder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException {

        // ✅ Load application.properties from config/ directory
        //System.setProperty("spring.config.location", "config/application.properties");

        // ✅ Let Spring Boot manage dependencies
        ApplicationContext context = SpringApplication.run(Main.class, args);

        // ✅ Check if department heads exist

        // ✅ Fetch and categorize emails
        System.out.println("📩 Fetching emails...");
        EmailFetcher emailFetcher = context.getBean(EmailFetcher.class);
        try {
            emailFetcher.fetchEmails();  // Fetch and store past emails
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        EmailCategorization emailCategorization = context.getBean(EmailCategorization.class);
        emailCategorization.categorizeAndStoreEmails();

        // ✅ Start automatic email forwarding
        EmailForwarder emailForwardingService = context.getBean(EmailForwarder.class);
        emailForwardingService.forwardCategorizedEmails();
    }
}
