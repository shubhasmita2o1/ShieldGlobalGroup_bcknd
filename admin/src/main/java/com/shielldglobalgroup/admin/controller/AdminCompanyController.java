package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.CompanyDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.Company;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
public class AdminCompanyController {

    private final CompanyService companyService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CompanyDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Companies loaded",
                contentMapper.toCompanyDTOList(companyService.getAll())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CompanyDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Companies loaded",
                contentMapper.toDTO(companyService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CompanyDTO>> create(@RequestBody CompanyDTO dto) {
        Company entity = contentMapper.toEntity(dto);
        Company saved = companyService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Companie created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CompanyDTO>> update(
            @PathVariable Long id,
            @RequestBody CompanyDTO dto) {
        Company entity = contentMapper.toEntity(dto);
        Company updated = companyService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Companie updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Companie deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<CompanyDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<Company> reordered = companyService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Companies reordered",
                contentMapper.toCompanyDTOList(reordered)));
    }
}
