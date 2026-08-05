package com.shielldglobalgroup.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapPinDTO {
    private Long id;
    private String label;
    private String leftPercent;
    private String topPercent;
}
