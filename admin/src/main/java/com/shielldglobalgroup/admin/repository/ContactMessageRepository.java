package com.shielldglobalgroup.admin.repository;
import com.shielldglobalgroup.admin.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findByIsReadFalseOrderBySubmittedAtDesc();

    List<ContactMessage> findAllByOrderBySubmittedAtDesc();

    Page<ContactMessage> findByIsRead(Boolean isRead, Pageable pageable);

    long countByIsReadFalse();

    long countByIsReadTrue();

    @Query("""
            SELECT m FROM ContactMessage m
            WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(m.email) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(m.phone) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(m.message) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<ContactMessage> searchByQuery(@Param("q") String q, Pageable pageable);

    @Query("""
            SELECT m FROM ContactMessage m
            WHERE m.isRead = :isRead
              AND (
                   LOWER(m.name) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.email) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.phone) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.message) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<ContactMessage> searchByIsReadAndQuery(
            @Param("isRead") Boolean isRead,
            @Param("q") String q,
            Pageable pageable);
}
