package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.HeroSlideDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.HeroSlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hero")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*")
public class HeroController {

    private final HeroSlideService heroSlideService;
    private final ContentMapper contentMapper;

    // GET /api/hero
    // home page calls this to load all 5 slides
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<HeroSlideDTO>>> getAllSlides() {

        List<HeroSlideDTO> slides = contentMapper.toHeroDTOList(
            heroSlideService.getAllSlides()
        );

        return ResponseEntity.ok(
            ApiResponseDTO.ok("Hero slides loaded", slides)
        );
    }
}
