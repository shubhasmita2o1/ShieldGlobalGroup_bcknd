package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.entity.ContactMessage;
import com.shielldglobalgroup.admin.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    // POST /api/contact
    // visitor submits contact form — no token needed
    // body: { "name":"John", "email":"john@x.com",
    //         "phone":"9876543210", "message":"Hello..." }
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> submitContact(
            @RequestBody ContactMessage message) {
        try {
            contactService.saveMessage(message);
            return ResponseEntity.ok(
                ApiResponseDTO.ok("Thank you! We will get back to you soon.")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponseDTO.error("Failed to send message. Please try again.")
            );
        }
    }
}
