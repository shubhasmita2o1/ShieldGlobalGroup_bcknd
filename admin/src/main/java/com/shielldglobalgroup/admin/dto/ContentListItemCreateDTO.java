package com.shielldglobalgroup.admin.dto;

import lombok.Data;

@Data
public class ContentListItemCreateDTO {
    private String pageKey;
    private String sectionKey;
    private String listType;
    private String itemText;
}