package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.ContactMessageDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminMessageController {

    private final ContactService contactService;
    private final ContentMapper contentMapper;

    // GET /api/admin/messages
    // admin inbox — all messages newest first
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ContactMessageDTO>>> getAllMessages() {

        List<ContactMessageDTO> messages = contactService.getAllMessages()
            .stream()
            .map(contentMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Messages loaded", messages)
        );
    }

    // GET /api/admin/messages/unread
    @GetMapping("/unread")
    public ResponseEntity<ApiResponseDTO<List<ContactMessageDTO>>> getUnread() {

        List<ContactMessageDTO> messages = contactService.getUnreadMessages()
            .stream()
            .map(contentMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Unread messages loaded", messages)
        );
    }

    // GET /api/admin/messages/unread/count
    // used for badge number on admin dashboard
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponseDTO<Long>> getUnreadCount() {
        return ResponseEntity.ok(
            ApiResponseDTO.ok("Unread count", contactService.countUnread())
        );
    }

    // PUT /api/admin/messages/{id}/read
    // admin clicks a message → marks it read
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponseDTO<ContactMessageDTO>> markAsRead(
            @PathVariable Long id) {

        var updated = contactService.markAsRead(id);
        var dto = contentMapper.toDTO(updated);

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Message marked as read", dto)
        );
    }
}