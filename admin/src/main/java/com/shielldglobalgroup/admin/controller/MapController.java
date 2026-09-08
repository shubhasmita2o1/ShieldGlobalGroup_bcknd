package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.MapLocationDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.MapLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapLocationService mapLocationService;
    private final ContentMapper contentMapper;

    /** Public: active locations only, ordered by displayOrder. */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MapLocationDTO>>> getActiveLocations() {
        List<MapLocationDTO> locations = contentMapper.toMapLocationDTOList(
                mapLocationService.getActive());
        return ResponseEntity.ok(ApiResponseDTO.ok("Locations loaded", locations));
    }

    /** Public: one active location, or 404. */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MapLocationDTO>> getActiveById(@PathVariable Long id) {
        MapLocationDTO dto = contentMapper.toDTO(mapLocationService.getActiveById(id));
        return ResponseEntity.ok(ApiResponseDTO.ok("Location loaded", dto));
    }
}