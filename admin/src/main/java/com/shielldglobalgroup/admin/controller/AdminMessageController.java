package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.ContactMessageDTO;
import com.shielldglobalgroup.admin.entity.ContactMessage;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/messages")
@RequiredArgsConstructor
public class AdminMessageController {

    private final ContactService contactService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getMessages(
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ContactMessage> result = contactService.search(isRead, search, page, size);
        List<ContactMessageDTO> content = contentMapper.toMessageDTOList(result.getContent());

        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("page", result.getNumber());
        data.put("size", result.getSize());
        data.put("totalElements", result.getTotalElements());
        data.put("totalPages", result.getTotalPages());

        return ResponseEntity.ok(ApiResponseDTO.ok("Messages loaded", data));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> stats() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Message stats", contactService.stats()));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponseDTO<List<ContactMessageDTO>>> getUnread() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Unread messages loaded",
                contentMapper.toMessageDTOList(contactService.getUnreadMessages())));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponseDTO<Long>> getUnreadCount() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Unread count", contactService.countUnread()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ContactMessageDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Message loaded",
                contentMapper.toDTO(contactService.getById(id))));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponseDTO<ContactMessageDTO>> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Message marked as read",
                contentMapper.toDTO(contactService.markAsRead(id))));
    }

    @PutMapping("/{id}/unread")
    public ResponseEntity<ApiResponseDTO<ContactMessageDTO>> markAsUnread(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Message marked as unread",
                contentMapper.toDTO(contactService.markAsUnread(id))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Message deleted"));
    }
}
