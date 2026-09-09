package com.shielldglobalgroup.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyMilestoneDTO {
    private Long id;
    private String year;
    private String indexLabel;
    private String name;
    private String subtitle;
    private String description;
    private String tag;
    private String flag;
    private Boolean isActive;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}