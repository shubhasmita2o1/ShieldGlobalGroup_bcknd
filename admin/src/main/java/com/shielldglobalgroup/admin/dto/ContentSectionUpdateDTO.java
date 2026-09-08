package com.shielldglobalgroup.admin.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ContentSectionUpdateDTO {
    private String pageKey;
    private String sectionKey;
    private Map<String, String> fields;
}