package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.JourneyMilestoneDTO;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.JourneyMilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final JourneyMilestoneService journeyMilestoneService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<JourneyMilestoneDTO>>> getActive() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Timeline loaded",
                contentMapper.toJourneyMilestoneDTOList(journeyMilestoneService.getActive())));
    }
}