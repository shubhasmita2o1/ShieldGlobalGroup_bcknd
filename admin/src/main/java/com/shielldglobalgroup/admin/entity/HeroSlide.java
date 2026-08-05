package com.shielldglobalgroup.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hero_slides")
@Data
public class HeroSlide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slide_order", nullable = false, unique = true)
    private Integer slideOrder;   // 1 to 5

    @Column(name = "title")
    private String title;         // "Shield Global Hr Solutions"

    @Column(name = "subtitle")
    private String subtitle;      // "Recruiting Manpower from Asia..."

    @Column(name = "video_path")
    private String videoPath;     // "Assets/HV2.mp4"

    @Column(name = "tab_label")
    private String tabLabel;      // "Global Manpower\nRecruitment"

    // slide_type: "intro" (slide 1) or "service" (slides 2-5)
    @Column(name = "slide_type")
    private String slideType;
}
