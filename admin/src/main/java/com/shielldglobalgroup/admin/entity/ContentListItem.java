package com.shielldglobalgroup.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "content_list_items")
@Data
public class ContentListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_key", nullable = false)
    private String pageKey;       // "about", "svc_manpower" etc.

    @Column(name = "section_key", nullable = false)
    private String sectionKey;    // "esg_ethical"

    @Column(name = "list_type", nullable = false)
    private String listType;      // "principles", "act_differently", "industries" etc.

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;   // 1, 2, 3... (controls display order)

    @Column(name = "item_text", columnDefinition = "TEXT", nullable = false)
    private String itemText;      // "Transparent hiring with no hidden costs"
}
