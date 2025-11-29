package org.example;

import jakarta.servlet.http.HttpServletRequest;
import org.example.EmailFetcher.model.LanDeviceEntity;
import org.example.EmailFetcher.repository.LanDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
@CrossOrigin(origins = {"http://localhost:3000","http://192.168.1.8:3000"}) // React frontend port
public class LanDeviceController {

    @Autowired
    private LanDeviceRepository deviceRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerDevice(@RequestBody LanDeviceEntity device, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        device.setIpAddress(ipAddress);
        deviceRepository.save(device);
        return ResponseEntity.ok("Device registered successfully!");
    }

    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        return ResponseEntity.ok(deviceRepository.findAll());
    }
}

