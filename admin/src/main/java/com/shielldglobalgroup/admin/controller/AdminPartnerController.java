package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.PartnerDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.Partner;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/partners")
@RequiredArgsConstructor
public class AdminPartnerController {

    private final PartnerService partnerService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PartnerDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Partners loaded",
                contentMapper.toPartnerDTOList(partnerService.getAll())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PartnerDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Partners loaded",
                contentMapper.toDTO(partnerService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<PartnerDTO>> create(@RequestBody PartnerDTO dto) {
        Partner entity = contentMapper.toEntity(dto);
        Partner saved = partnerService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Partner created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PartnerDTO>> update(
            @PathVariable Long id,
            @RequestBody PartnerDTO dto) {
        Partner entity = contentMapper.toEntity(dto);
        Partner updated = partnerService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Partner updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        partnerService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Partner deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<PartnerDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<Partner> reordered = partnerService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Partners reordered",
                contentMapper.toPartnerDTOList(reordered)));
    }
}
