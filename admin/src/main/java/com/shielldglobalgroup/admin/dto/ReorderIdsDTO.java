package com.shielldglobalgroup.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReorderIdsDTO {
    private List<Long> orderedIds;
}