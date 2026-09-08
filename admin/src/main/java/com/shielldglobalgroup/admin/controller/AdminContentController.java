package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.*;
import com.shielldglobalgroup.admin.entity.ContentListItem;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final ContentService contentService;
    private final ContentMapper contentMapper;

    // ── Content blocks ────────────────────────────────────────────────────

    @GetMapping("/page/{pageKey}")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getPage(
            @PathVariable String pageKey) {
        return ResponseEntity.ok(
                ApiResponseDTO.ok("Content loaded for page: " + pageKey,
                        contentService.getPageContent(pageKey)));
    }

    @GetMapping("/section/{pageKey}/{sectionKey}")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getSection(
            @PathVariable String pageKey,
            @PathVariable String sectionKey) {
        return ResponseEntity.ok(
                ApiResponseDTO.ok("Section loaded",
                        contentService.getSectionContent(pageKey, sectionKey)));
    }

    @PutMapping("/field")
    public ResponseEntity<ApiResponseDTO<Void>> saveField(@RequestBody ContentBlockDTO dto) {
        if (dto.getPageKey() == null || dto.getSectionKey() == null || dto.getField() == null) {
            throw new IllegalArgumentException("pageKey, sectionKey and field are required");
        }
        contentService.saveField(dto.getPageKey(), dto.getSectionKey(), dto.getField(), dto.getValue());
        return ResponseEntity.ok(ApiResponseDTO.ok("Field saved successfully"));
    }

    @PutMapping("/section")
    public ResponseEntity<ApiResponseDTO<Void>> saveSection(@RequestBody ContentSectionUpdateDTO body) {
        if (body.getPageKey() == null || body.getSectionKey() == null) {
            throw new IllegalArgumentException("pageKey and sectionKey are required");
        }
        contentService.saveSectionFields(body.getPageKey(), body.getSectionKey(), body.getFields());
        return ResponseEntity.ok(ApiResponseDTO.ok("Section saved successfully"));
    }

    @DeleteMapping("/field")
    public ResponseEntity<ApiResponseDTO<Void>> deleteField(
            @RequestParam String pageKey,
            @RequestParam String sectionKey,
            @RequestParam String field) {
        contentService.deleteField(pageKey, sectionKey, field);
        return ResponseEntity.ok(ApiResponseDTO.ok("Field deleted"));
    }

    // ── Content list items ────────────────────────────────────────────────

    @GetMapping("/list/{pageKey}/{sectionKey}/{listType}")
    public ResponseEntity<ApiResponseDTO<List<ContentListItemDTO>>> getList(
            @PathVariable String pageKey,
            @PathVariable String sectionKey,
            @PathVariable String listType) {
        List<ContentListItemDTO> dtos = contentMapper.toContentListItemDTOList(
                contentService.getListItems(pageKey, sectionKey, listType));
        return ResponseEntity.ok(ApiResponseDTO.ok("List loaded", dtos));
    }

    @PutMapping("/list")
    public ResponseEntity<ApiResponseDTO<Void>> replaceList(@RequestBody ContentListReplaceDTO body) {
        if (body.getPageKey() == null || body.getSectionKey() == null || body.getListType() == null) {
            throw new IllegalArgumentException("pageKey, sectionKey and listType are required");
        }
        contentService.saveListItems(body.getPageKey(), body.getSectionKey(),
                body.getListType(), body.getItems());
        return ResponseEntity.ok(ApiResponseDTO.ok("List saved successfully"));
    }

    @PutMapping("/list/reorder")
    public ResponseEntity<ApiResponseDTO<Void>> reorderList(@RequestBody ContentListReorderDTO body) {
        if (body.getPageKey() == null || body.getSectionKey() == null || body.getListType() == null) {
            throw new IllegalArgumentException("pageKey, sectionKey and listType are required");
        }
        contentService.reorderListItems(body.getPageKey(), body.getSectionKey(),
                body.getListType(), body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok("List reordered"));
    }

    @PostMapping("/list/item")
    public ResponseEntity<ApiResponseDTO<ContentListItemDTO>> appendItem(
            @RequestBody ContentListItemCreateDTO body) {
        if (body.getPageKey() == null || body.getSectionKey() == null
                || body.getListType() == null || body.getItemText() == null) {
            throw new IllegalArgumentException("pageKey, sectionKey, listType and itemText are required");
        }
        ContentListItem saved = contentService.appendListItem(
                body.getPageKey(), body.getSectionKey(), body.getListType(), body.getItemText());
        return ResponseEntity.ok(ApiResponseDTO.ok("Item added", contentMapper.toDTO(saved)));
    }

    @PutMapping("/list/item/{id}")
    public ResponseEntity<ApiResponseDTO<ContentListItemDTO>> updateItem(
            @PathVariable Long id,
            @RequestBody ContentListItemUpdateDTO body) {
        if (body.getItemText() == null) {
            throw new IllegalArgumentException("itemText is required");
        }
        ContentListItem updated = contentService.updateListItemText(id, body.getItemText());
        return ResponseEntity.ok(ApiResponseDTO.ok("Item updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/list/item/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteItem(@PathVariable Long id) {
        contentService.deleteListItem(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Item deleted"));
    }
}
