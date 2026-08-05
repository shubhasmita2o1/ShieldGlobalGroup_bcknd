package com.shielldglobalgroup.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentBlockDTO {
    private String pageKey;
    private String sectionKey;
    private String field;
    private String value;
}
