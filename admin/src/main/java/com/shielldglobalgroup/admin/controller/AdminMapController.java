package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.MapPinDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.MapPinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/map")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminMapController {

    private final MapPinService mapPinService;
    private final ContentMapper contentMapper;

    // GET /api/admin/map
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MapPinDTO>>> getAllPins() {
        List<MapPinDTO> pins = contentMapper.toMapPinDTOList(
            mapPinService.getAllPins()
        );
        return ResponseEntity.ok(ApiResponseDTO.ok("Pins loaded", pins));
    }

    // POST /api/admin/map
    // body: { "label":"Tokyo", "leftPercent":"72%", "topPercent":"38%" }
    @PostMapping
    public ResponseEntity<ApiResponseDTO<MapPinDTO>> addPin(
            @RequestBody MapPinDTO dto) {

        var entity = contentMapper.toEntity(dto);
        var saved = mapPinService.addPin(entity);
        var savedDTO = contentMapper.toDTO(saved);

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Pin added", savedDTO)
        );
    }

    // PUT /api/admin/map/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MapPinDTO>> updatePin(
            @PathVariable Long id,
            @RequestBody MapPinDTO dto) {

        var entity = contentMapper.toEntity(dto);
        var updated = mapPinService.updatePin(id, entity);
        var updatedDTO = contentMapper.toDTO(updated);

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Pin updated", updatedDTO)
        );
    }

    // DELETE /api/admin/map/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deletePin(
            @PathVariable Long id) {

        mapPinService.deletePin(id);
        return ResponseEntity.ok(
            ApiResponseDTO.ok("Pin deleted")
        );
    }
}
