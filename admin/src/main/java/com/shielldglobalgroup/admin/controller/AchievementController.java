package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.AchievementDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AchievementDTO>>> getActive() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Achievements loaded",
                contentMapper.toAchievementDTOList(achievementService.getActive())));
    }
}
