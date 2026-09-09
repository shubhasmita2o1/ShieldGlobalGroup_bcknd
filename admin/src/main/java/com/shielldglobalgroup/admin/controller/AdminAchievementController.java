package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.AchievementDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.Achievement;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/achievements")
@RequiredArgsConstructor
public class AdminAchievementController {

    private final AchievementService achievementService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AchievementDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Achievements loaded",
                contentMapper.toAchievementDTOList(achievementService.getAll())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AchievementDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Achievements loaded",
                contentMapper.toDTO(achievementService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<AchievementDTO>> create(@RequestBody AchievementDTO dto) {
        Achievement entity = contentMapper.toEntity(dto);
        Achievement saved = achievementService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Achievement created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AchievementDTO>> update(
            @PathVariable Long id,
            @RequestBody AchievementDTO dto) {
        Achievement entity = contentMapper.toEntity(dto);
        Achievement updated = achievementService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Achievement updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        achievementService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Achievement deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<AchievementDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<Achievement> reordered = achievementService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Achievements reordered",
                contentMapper.toAchievementDTOList(reordered)));
    }
}
