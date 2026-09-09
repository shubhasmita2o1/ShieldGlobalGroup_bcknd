package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.JourneyMilestoneDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.JourneyMilestone;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.JourneyMilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/timeline")
@RequiredArgsConstructor
public class AdminTimelineController {

    private final JourneyMilestoneService journeyMilestoneService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<JourneyMilestoneDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Timeline loaded",
                contentMapper.toJourneyMilestoneDTOList(journeyMilestoneService.getAll())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<JourneyMilestoneDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Timeline loaded",
                contentMapper.toDTO(journeyMilestoneService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<JourneyMilestoneDTO>> create(@RequestBody JourneyMilestoneDTO dto) {
        JourneyMilestone entity = contentMapper.toEntity(dto);
        JourneyMilestone saved = journeyMilestoneService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Timeline created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<JourneyMilestoneDTO>> update(
            @PathVariable Long id,
            @RequestBody JourneyMilestoneDTO dto) {
        JourneyMilestone entity = contentMapper.toEntity(dto);
        JourneyMilestone updated = journeyMilestoneService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Timeline updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        journeyMilestoneService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Timeline deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<JourneyMilestoneDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<JourneyMilestone> reordered = journeyMilestoneService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Timeline reordered",
                contentMapper.toJourneyMilestoneDTOList(reordered)));
    }
}
