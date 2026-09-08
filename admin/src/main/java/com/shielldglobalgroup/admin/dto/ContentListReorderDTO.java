package com.shielldglobalgroup.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContentListReorderDTO {
    private String pageKey;
    private String sectionKey;
    private String listType;
    private List<Long> orderedIds;
}