package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.HeroSlideDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.HeroSlide;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.HeroSlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/hero")
@RequiredArgsConstructor
public class AdminHeroController {

    private final HeroSlideService heroSlideService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<HeroSlideDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Hero slides loaded",
                contentMapper.toHeroDTOList(heroSlideService.getAllSlides())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<HeroSlideDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Hero slide loaded",
                contentMapper.toDTO(heroSlideService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<HeroSlideDTO>> create(@RequestBody HeroSlideDTO dto) {
        HeroSlide entity = contentMapper.toEntity(dto);
        HeroSlide saved = heroSlideService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Hero slide created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<HeroSlideDTO>> update(
            @PathVariable Long id,
            @RequestBody HeroSlideDTO dto) {
        HeroSlide entity = contentMapper.toEntity(dto);
        HeroSlide updated = heroSlideService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Hero slide updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        heroSlideService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Hero slide deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<HeroSlideDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<HeroSlide> reordered = heroSlideService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Hero slides reordered",
                contentMapper.toHeroDTOList(reordered)));
    }
}
