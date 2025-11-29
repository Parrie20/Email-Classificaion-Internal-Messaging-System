package org.example.EmailFetcher;

import org.example.EmailFetcher.model.MessageEntity;
import org.example.EmailFetcher.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageEntity sendMessage(String senderEmail, String receiverEmail, String content) {
        MessageEntity message = new MessageEntity(senderEmail, receiverEmail, content, LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<MessageEntity> getInbox(String receiverEmail) {
        return messageRepository.findByReceiverEmail(receiverEmail);
    }
}
