package com.shielldglobalgroup.admin.service;
import com.shielldglobalgroup.admin.entity.ContactMessage;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactRepo;

    public ContactMessage saveMessage(ContactMessage message) {
        return contactRepo.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return contactRepo.findAllByOrderBySubmittedAtDesc();
    }

    public List<ContactMessage> getUnreadMessages() {
        return contactRepo.findByIsReadFalseOrderBySubmittedAtDesc();
    }

    public ContactMessage getById(Long id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", id));
    }

    public Page<ContactMessage> search(Boolean isRead, String search, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "submittedAt"));

        String q = (search == null || search.isBlank()) ? null : search.trim();

        if (q != null && isRead != null) {
            return contactRepo.searchByIsReadAndQuery(isRead, q, pageable);
        }
        if (q != null) {
            return contactRepo.searchByQuery(q, pageable);
        }
        if (isRead != null) {
            return contactRepo.findByIsRead(isRead, pageable);
        }
        return contactRepo.findAll(pageable);
    }

    @Transactional
    public ContactMessage markAsRead(Long id) {
        ContactMessage msg = getById(id);
        msg.setIsRead(true);
        return contactRepo.save(msg);
    }

    @Transactional
    public ContactMessage markAsUnread(Long id) {
        ContactMessage msg = getById(id);
        msg.setIsRead(false);
        return contactRepo.save(msg);
    }

    @Transactional
    public void delete(Long id) {
        ContactMessage msg = getById(id);
        contactRepo.delete(msg);
    }

    public long countUnread() {
        return contactRepo.countByIsReadFalse();
    }

    public long countRead() {
        return contactRepo.countByIsReadTrue();
    }

    public Map<String, Long> stats() {
        Map<String, Long> map = new HashMap<>();
        long total = contactRepo.count();
        long unread = countUnread();
        map.put("total", total);
        map.put("unread", unread);
        map.put("read", total - unread);
        return map;
    }
}