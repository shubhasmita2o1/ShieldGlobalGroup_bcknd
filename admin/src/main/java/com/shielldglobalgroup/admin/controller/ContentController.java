package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.ContentListItemDTO;
import com.shielldglobalgroup.admin.entity.ContentListItem;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*")
public class ContentController {

    private final ContentService contentService;
    private final ContentMapper contentMapper;

    // GET /api/content/page/home
    // GET /api/content/page/about
    // GET /api/content/page/svc_manpower
    // GET /api/content/page/svc_staffing
    // GET /api/content/page/svc_ai
    // GET /api/content/page/svc_media
    // GET /api/content/page/contact
    // GET /api/content/page/shared
    // ONE endpoint serves ALL 8 pages
    @GetMapping("/page/{pageKey}")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getPageContent(
            @PathVariable String pageKey) {

        Map<String, String> content = contentService.getPageContent(pageKey);

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Content loaded for page: " + pageKey, content)
        );
    }

    // GET /api/content/section/about/founders
    // GET /api/content/section/about/esg_ethical
    // GET /api/content/section/svc_manpower/intro
    @GetMapping("/section/{pageKey}/{sectionKey}")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getSectionContent(
            @PathVariable String pageKey,
            @PathVariable String sectionKey) {

        Map<String, String> content = contentService.getSectionContent(
            pageKey, sectionKey
        );

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Section loaded", content)
        );
    }

    // GET /api/content/list/about/esg_ethical/principles
    // GET /api/content/list/svc_manpower/intro/industries
    @GetMapping("/list/{pageKey}/{sectionKey}/{listType}")
    public ResponseEntity<ApiResponseDTO<List<ContentListItemDTO>>> getListItems(
            @PathVariable String pageKey,
            @PathVariable String sectionKey,
            @PathVariable String listType) {

        List<ContentListItem> items = contentService.getListItems(
            pageKey, sectionKey, listType
        );

        List<ContentListItemDTO> dtos = items.stream()
            .map(contentMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            ApiResponseDTO.ok("List loaded", dtos)
        );
    }
}
