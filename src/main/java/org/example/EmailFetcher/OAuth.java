package org.example.EmailFetcher;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class OAuth {
    private static final String CLIENT_SECRET_FILE = "OAuth_credentials.json"; // Ensure this is in resources
    private static final List<String> SCOPES = Collections.singletonList("https://mail.google.com/");
    private static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.home") + "/gmail_tokens"; // Store tokens persistently

    private static Credential credential;

    public static Credential getCredentials() throws IOException, GeneralSecurityException {
        if (credential != null && credential.getAccessToken() != null && !credential.getAccessToken().isEmpty()) {
            if (credential.getExpirationTimeMilliseconds() != null && credential.getExpirationTimeMilliseconds() > System.currentTimeMillis()) {
                return credential; // Return valid token
            } else {
                credential.refreshToken(); // Refresh if expired
                return credential;
            }
        }

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Load client secrets
        InputStream in = OAuth.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE);
        if (in == null) {
            throw new FileNotFoundException("Resource 'OAuth_credentials.json' not found in classpath.");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build authorization flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH))) // Save tokens persistently
                .setAccessType("offline") // Get refresh token for long-term use
                .build();

        // Retrieve stored credentials (if available)
        credential = flow.loadCredential("user");
        if (credential != null) {
            if (credential.getExpirationTimeMilliseconds() != null && credential.getExpirationTimeMilliseconds() > System.currentTimeMillis()) {
                return credential; // Return valid token
            } else {
                credential.refreshToken(); // Refresh if expired
                return credential;
            }
        }

        // Start authentication flow (if no stored credentials)
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return credential;
    }

    public static String getAccessToken() throws IOException, GeneralSecurityException {
        return getCredentials().getAccessToken();
    }
}
