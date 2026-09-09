package com.shielldglobalgroup.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestimonialDTO {
    private Long id;
    private String name;
    private String designation;
    private String company;
    private String message;
    private String photoUrl;
    private Integer rating;
    private Boolean isActive;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
