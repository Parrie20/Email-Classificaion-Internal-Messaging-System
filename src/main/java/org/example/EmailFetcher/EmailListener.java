package org.example.EmailFetcher;

import jakarta.mail.*;
import jakarta.mail.event.MessageCountAdapter;
import jakarta.mail.event.MessageCountEvent;
import com.sun.mail.imap.IMAPFolder;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailListener {

    public static void startListening() {  // ✅ Make it static
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", "imap.gmail.com");
            properties.put("mail.imaps.port", "993");
            properties.put("mail.imaps.ssl.enable", "true");
            properties.put("mail.debug", "true"); // Enable debugging

            Session session = Session.getInstance(properties);
            Store store = session.getStore("imaps");
            store.connect("testerid0320@gmail.com", "Test@0320");

            IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            inbox.addMessageCountListener(new MessageCountAdapter() {
                @Override
                public void messagesAdded(MessageCountEvent event) {
                    System.out.println("📩 New email received!");
                }
            });

            System.out.println("📡 Listening for new emails...");
            while (true) {
                inbox.idle(); // ✅ Works because we use IMAPFolder
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
