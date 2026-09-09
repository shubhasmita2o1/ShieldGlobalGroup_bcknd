package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.TestimonialDTO;
import com.shielldglobalgroup.admin.dto.ReorderIdsDTO;
import com.shielldglobalgroup.admin.entity.Testimonial;
import com.shielldglobalgroup.admin.mapper.ContentMapper;
import com.shielldglobalgroup.admin.service.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/testimonials")
@RequiredArgsConstructor
public class AdminTestimonialController {

    private final TestimonialService testimonialService;
    private final ContentMapper contentMapper;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TestimonialDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Testimonials loaded",
                contentMapper.toTestimonialDTOList(testimonialService.getAll())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TestimonialDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Testimonials loaded",
                contentMapper.toDTO(testimonialService.getById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TestimonialDTO>> create(@RequestBody TestimonialDTO dto) {
        Testimonial entity = contentMapper.toEntity(dto);
        Testimonial saved = testimonialService.create(entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Testimonial created", contentMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TestimonialDTO>> update(
            @PathVariable Long id,
            @RequestBody TestimonialDTO dto) {
        Testimonial entity = contentMapper.toEntity(dto);
        Testimonial updated = testimonialService.update(id, entity);
        return ResponseEntity.ok(ApiResponseDTO.ok("Testimonial updated", contentMapper.toDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        testimonialService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Testimonial deleted"));
    }

    @PutMapping("/reorder")
    public ResponseEntity<ApiResponseDTO<List<TestimonialDTO>>> reorder(@RequestBody ReorderIdsDTO body) {
        List<Testimonial> reordered = testimonialService.reorder(body.getOrderedIds());
        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Testimonials reordered",
                contentMapper.toTestimonialDTOList(reordered)));
        }
    }