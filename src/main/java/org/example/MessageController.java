package org.example;

import org.example.EmailFetcher.model.MessageEntity;
import org.example.EmailFetcher.MessageService;
import org.example.EmailFetcher.repository.LanDeviceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"http://localhost:3000","http://192.168.1.8:3000"})
public class MessageController {

    private final MessageService messageService;
    private final LanDeviceRepository lanDeviceRepository;

    public MessageController(MessageService messageService, LanDeviceRepository lanDeviceRepository) {
        this.messageService = messageService;
        this.lanDeviceRepository = lanDeviceRepository;
    }

    @PostMapping("/send")
    public MessageEntity sendMessage(@RequestBody Map<String, String> body) {
        return messageService.sendMessage(body.get("senderEmail"), body.get("receiverEmail"), body.get("content"));
    }

    @GetMapping("/inbox/{receiverEmail}")
    public List<MessageEntity> getInbox(@PathVariable String receiverEmail) {
        return messageService.getInbox(receiverEmail);
    }

    @GetMapping("/check-device/{email}")
    public boolean isDeviceRegistered(@PathVariable String email) {
        return lanDeviceRepository.existsByEmail(email); // ✅ Correct usage
    }
}