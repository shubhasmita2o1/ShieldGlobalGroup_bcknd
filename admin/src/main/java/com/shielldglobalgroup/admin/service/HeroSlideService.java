package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.HeroSlide;
import com.shielldglobalgroup.admin.repository.HeroSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeroSlideService {
    private final HeroSlideRepository heroRepo;

    // Get all slides in order 1→5
    public List<HeroSlide> getAllSlides() {
        return heroRepo.findAllByOrderBySlideOrderAsc();
    }

    // Get one slide by order number
    public HeroSlide getSlide(Integer slideOrder) {
        return heroRepo.findBySlideOrder(slideOrder)
            .orElseThrow(() -> new com.shielldglobalgroup.admin.exception.ResourceNotFoundException("Slide", slideOrder));
    }

    // Save or update a slide
    public HeroSlide saveSlide(HeroSlide slide) {
        // if slide with this order exists, update it
        heroRepo.findBySlideOrder(slide.getSlideOrder())
            .ifPresent(existing -> slide.setId(existing.getId()));
        return heroRepo.save(slide);
    }

    // Update only the video path (after file upload)
    public HeroSlide updateVideoPath(Integer slideOrder, String videoPath) {
        HeroSlide slide = getSlide(slideOrder);
        slide.setVideoPath(videoPath);
        return heroRepo.save(slide);
    }
}
