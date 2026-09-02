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
@RequestMapping("/api/map")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*")
public class MapController {

    private final MapPinService mapPinService;
    private final ContentMapper contentMapper;

    // GET /api/map
    // home page calls this to load all country pins
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MapPinDTO>>> getAllPins() {

        List<MapPinDTO> pins = contentMapper.toMapPinDTOList(
            mapPinService.getAllPins()
        );

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Map pins loaded", pins)
        );
    }
}
