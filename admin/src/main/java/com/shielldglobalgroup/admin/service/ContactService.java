package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.ContactMessage;
import com.shielldglobalgroup.admin.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactMessageRepository contactRepo;

        // Save new message from contact form
    public ContactMessage saveMessage(ContactMessage message) {
        return contactRepo.save(message);
    }

    // Get all messages (admin inbox) newest first
    public List<ContactMessage> getAllMessages() {
        return contactRepo.findAllByOrderBySubmittedAtDesc();
    }

    // Get only unread messages
    public List<ContactMessage> getUnreadMessages() {
        return contactRepo.findByIsReadFalseOrderBySubmittedAtDesc();
    }

    // Mark a message as read
    public ContactMessage markAsRead(Long id) {
        ContactMessage msg = contactRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found: " + id));
        msg.setIsRead(true);
        return contactRepo.save(msg);
    }

    // Count unread (for admin dashboard badge)
    public long countUnread() {
        return contactRepo.findByIsReadFalseOrderBySubmittedAtDesc().size();
    }

}
