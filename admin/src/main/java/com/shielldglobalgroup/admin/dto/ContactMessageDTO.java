package com.shielldglobalgroup.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String message;
    private LocalDateTime submittedAt;
    private Boolean isRead;
}
