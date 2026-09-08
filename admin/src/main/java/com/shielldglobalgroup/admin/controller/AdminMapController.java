package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.MapLocationDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.MapLocation;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.MapLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/map")
@RequiredArgsConstructor
public class AdminMapController {

    private final MapLocationService mapLocationService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MapLocationDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Locations loaded",
                contentMapper.toMapLocationDTOList(mapLocationService.getAll())));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<MapLocationDTO>>> getActive() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Active locations loaded",
                contentMapper.toMapLocationDTOList(mapLocationService.getActive())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MapLocationDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Location loaded",
                contentMapper.toDTO(mapLocationService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<MapLocationDTO>> create(@RequestBody MapLocationDTO dto) {
        MapLocation entity = contentMapper.toEntity(dto);
        MapLocation saved = mapLocationService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Location created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MapLocationDTO>> update(
            @PathVariable Long id,
            @RequestBody MapLocationDTO dto) {
        MapLocation entity = contentMapper.toEntity(dto);
        MapLocation updated = mapLocationService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Location updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        mapLocationService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Location deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<MapLocationDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<MapLocation> reordered = mapLocationService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Locations reordered",
                contentMapper.toMapLocationDTOList(reordered)));
    }
}
