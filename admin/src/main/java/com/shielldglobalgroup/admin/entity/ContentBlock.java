package com.shielldglobalgroup.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "content_blocks",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"page_key", "section_key", "field"}
       ))
@Data
public class ContentBlock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_key", nullable = false)
    private String pageKey; 

    @Column(name = "section_key", nullable = false)
    private String sectionKey; 

    @Column(name = "field", nullable = false)
    private String field; 

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;
    }
