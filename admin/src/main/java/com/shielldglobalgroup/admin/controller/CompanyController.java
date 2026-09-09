package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.CompanyDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CompanyDTO>>> getActive() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Companies loaded",
                contentMapper.toCompanyDTOList(companyService.getActive())));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponseDTO<CompanyDTO>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Company loaded",
                contentMapper.toDTO(companyService.getActiveBySlug(slug))));
    }
}
