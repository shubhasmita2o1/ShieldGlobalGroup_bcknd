package com.shielldglobalgroup.admin.repository;
import com.shielldglobalgroup.admin.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    // Get all unread messages (admin inbox — shows unread first)
    List<ContactMessage> findByIsReadFalseOrderBySubmittedAtDesc();

    // Get all messages newest first
    List<ContactMessage> findAllByOrderBySubmittedAtDesc(); 
}
