package com.shielldglobalgroup.admin.dto;

import java.util.List;

import lombok.Data;

@Data
public class ContentListReplaceDTO {
    private String pageKey;
    private String sectionKey;
    private String listType;
    private List<String> items;
}
