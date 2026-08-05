package com.shielldglobalgroup.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "map_pins")
@Data
public class MapPin {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;         // "Mumbai", "Dubai", "Singapore"

    @Column(name = "left_percent", nullable = false)
    private String leftPercent;   // "59.9%"

    @Column(name = "top_percent", nullable = false)
    private String topPercent;    // "50.5%"
}
