package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.HeroSlide;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.HeroSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HeroSlideService {

    private final HeroSlideRepository heroRepo;

    public List<HeroSlide> getAllSlides() {
        return heroRepo.findAllByOrderBySlideOrderAsc();
    }

    public HeroSlide getById(Long id) {
        return heroRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hero slide", id));
    }

    public HeroSlide getSlide(Integer slideOrder) {
        return heroRepo.findBySlideOrder(slideOrder)
                .orElseThrow(() -> new ResourceNotFoundException("Slide", slideOrder));
    }

    @Transactional
    public HeroSlide create(HeroSlide slide) {
        if (slide.getSlideOrder() == null) {
            List<HeroSlide> all = getAllSlides();
            int next = all.isEmpty() ? 1 : all.get(all.size() - 1).getSlideOrder() + 1;
            slide.setSlideOrder(next);
        } else if (heroRepo.findBySlideOrder(slide.getSlideOrder()).isPresent()) {
            throw new IllegalArgumentException("slideOrder already exists: " + slide.getSlideOrder());
        }
        slide.setId(null);
        return heroRepo.save(slide);
    }

    @Transactional
    public HeroSlide update(Long id, HeroSlide incoming) {
        HeroSlide existing = getById(id);

        if (incoming.getSlideOrder() != null
                && !incoming.getSlideOrder().equals(existing.getSlideOrder())) {
            heroRepo.findBySlideOrder(incoming.getSlideOrder()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new IllegalArgumentException(
                            "slideOrder already exists: " + incoming.getSlideOrder());
                }
            });
            existing.setSlideOrder(incoming.getSlideOrder());
        }

        if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
        if (incoming.getSubtitle() != null) existing.setSubtitle(incoming.getSubtitle());
        if (incoming.getVideoPath() != null) existing.setVideoPath(incoming.getVideoPath());
        if (incoming.getTabLabel() != null) existing.setTabLabel(incoming.getTabLabel());
        if (incoming.getSlideType() != null) existing.setSlideType(incoming.getSlideType());

        return heroRepo.save(existing);
    }

    /** Legacy upsert by slideOrder (kept for compatibility). */
    @Transactional
    public HeroSlide saveSlide(HeroSlide slide) {
        heroRepo.findBySlideOrder(slide.getSlideOrder())
                .ifPresent(existing -> slide.setId(existing.getId()));
        return heroRepo.save(slide);
    }

    @Transactional
    public HeroSlide updateVideoPath(Integer slideOrder, String videoPath) {
        HeroSlide slide = getSlide(slideOrder);
        slide.setVideoPath(videoPath);
        return heroRepo.save(slide);
    }

    @Transactional
    public void delete(Long id) {
        HeroSlide slide = getById(id);
        heroRepo.delete(slide);
        normalizeOrders();
    }

    @Transactional
    public List<HeroSlide> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }

        List<HeroSlide> all = getAllSlides();
        Map<Long, HeroSlide> byId = new HashMap<>();
        for (HeroSlide s : all) {
            byId.put(s.getId(), s);
        }

        if (orderedIds.size() != all.size()) {
            throw new IllegalArgumentException(
                    "orderedIds size must match total slides (" + all.size() + ")");
        }

        for (Long id : orderedIds) {
            if (!byId.containsKey(id)) {
                throw new IllegalArgumentException("Unknown hero slide id: " + id);
            }
        }

        // Two-phase update to avoid unique constraint collisions on slide_order
        for (int i = 0; i < orderedIds.size(); i++) {
            HeroSlide s = byId.get(orderedIds.get(i));
            s.setSlideOrder(10_000 + i);
            heroRepo.save(s);
        }
        heroRepo.flush();

        for (int i = 0; i < orderedIds.size(); i++) {
            HeroSlide s = byId.get(orderedIds.get(i));
            s.setSlideOrder(i + 1);
            heroRepo.save(s);
        }

        return getAllSlides();
    }

    private void normalizeOrders() {
        List<HeroSlide> all = getAllSlides();
        for (int i = 0; i < all.size(); i++) {
            HeroSlide s = all.get(i);
            int expected = i + 1;
            if (s.getSlideOrder() == null || s.getSlideOrder() != expected) {
                s.setSlideOrder(expected);
                heroRepo.save(s);
            }
        }
    }
}
