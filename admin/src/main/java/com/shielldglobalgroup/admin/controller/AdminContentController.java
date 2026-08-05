package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.ContentBlockDTO;
import com.shielldglobalgroup.admin.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminContentController {

    private final ContentService contentService;

    // PUT /api/admin/content/field
    // saves ONE field
    // body: { "pageKey":"about", "sectionKey":"founders",
    //         "field":"para_1", "value":"new text..." }
    @PutMapping("/field")
    public ResponseEntity<ApiResponseDTO<Void>> saveField(
            @RequestBody ContentBlockDTO dto) {

        contentService.saveField(
            dto.getPageKey(),
            dto.getSectionKey(),
            dto.getField(),
            dto.getValue()
        );

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Field saved successfully")
        );
    }

    // PUT /api/admin/content/section
    // saves ALL fields of a section at once
    // body: {
    //   "pageKey": "about",
    //   "sectionKey": "founders",
    //   "fields": {
    //     "heading": "Founder's Message",
    //     "para_1": "At Shield Global...",
    //     "para_2": "Our objective...",
    //     "signature": "— Founder, Shield Global Group"
    //   }
    // }
    @PutMapping("/section")
    public ResponseEntity<ApiResponseDTO<Void>> saveSection(
            @RequestBody Map<String, Object> body) {

        String pageKey = (String) body.get("pageKey");
        String sectionKey = (String) body.get("sectionKey");

        @SuppressWarnings("unchecked")
        Map<String, String> fields = (Map<String, String>) body.get("fields");

        contentService.saveSectionFields(pageKey, sectionKey, fields);

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Section saved successfully")
        );
    }

    // PUT /api/admin/content/list
    // replaces entire bullet list
    // body: {
    //   "pageKey": "about",
    //   "sectionKey": "esg_ethical",
    //   "listType": "principles",
    //   "items": [
    //     "Transparent hiring with no hidden costs",
    //     "Compliance with international labor laws",
    //     "Equal opportunity practices"
    //   ]
    // }
    @PutMapping("/list")
    public ResponseEntity<ApiResponseDTO<Void>> saveList(
            @RequestBody Map<String, Object> body) {

        String pageKey = (String) body.get("pageKey");
        String sectionKey = (String) body.get("sectionKey");
        String listType = (String) body.get("listType");

        @SuppressWarnings("unchecked")
        List<String> items = (List<String>) body.get("items");

        contentService.saveListItems(pageKey, sectionKey, listType, items);

        return ResponseEntity.ok(
            ApiResponseDTO.ok("List saved successfully")
        );
    }
}
