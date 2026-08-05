package com.shielldglobalgroup.admin.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroSlideDTO {
    private Integer slideOrder;
    private String title;
    private String subtitle;
    private String videoPath;
    private String tabLabel;
    private String slideType;
}
