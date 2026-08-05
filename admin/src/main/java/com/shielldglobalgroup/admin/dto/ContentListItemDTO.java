package com.shielldglobalgroup.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentListItemDTO {
    private String pageKey;
    private String sectionKey;
    private String listType;
    private Integer orderIndex;
    private String itemText;
    // NO id — frontend doesn't need it
}
