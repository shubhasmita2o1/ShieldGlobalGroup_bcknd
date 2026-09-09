package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.Testimonial;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository repo;

    public List<Testimonial> getAll() {
        return repo.findAllByOrderByDisplayOrderAsc();
    }

    public List<Testimonial> getActive() {
        return repo.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Testimonial getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial", id));
    }

    @Transactional
    public Testimonial create(Testimonial incoming) {
        validate(incoming, true);
        if (incoming.getDisplayOrder() == null) {
            List<Testimonial> all = getAll();
            int next = all.isEmpty() ? 1 : all.get(all.size() - 1).getDisplayOrder() + 1;
            incoming.setDisplayOrder(next);
        }
        if (incoming.getIsActive() == null) {
            incoming.setIsActive(true);
        }
        incoming.setId(null);
        return repo.save(incoming);
    }

    @Transactional
    public Testimonial update(Long id, Testimonial incoming) {
        Testimonial existing = getById(id);
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getDesignation() != null) existing.setDesignation(incoming.getDesignation());
        if (incoming.getCompany() != null) existing.setCompany(incoming.getCompany());
        if (incoming.getMessage() != null) existing.setMessage(incoming.getMessage());
        if (incoming.getPhotoUrl() != null) existing.setPhotoUrl(incoming.getPhotoUrl());
        if (incoming.getRating() != null) existing.setRating(incoming.getRating());
        if (incoming.getIsActive() != null) existing.setIsActive(incoming.getIsActive());
        if (incoming.getDisplayOrder() != null) existing.setDisplayOrder(incoming.getDisplayOrder());
        validate(existing, false);
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Testimonial entity = getById(id);
        repo.delete(entity);
        normalizeOrder();
    }

    @Transactional
    public List<Testimonial> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }
        List<Testimonial> all = getAll();
        Map<Long, Testimonial> byId = new HashMap<>();
        for (Testimonial e : all) {
            byId.put(e.getId(), e);
        }
        if (orderedIds.size() != all.size()) {
            throw new IllegalArgumentException(
                    "orderedIds size must match total items (" + all.size() + ")");
        }
        for (Long oid : orderedIds) {
            if (!byId.containsKey(oid)) {
                throw new IllegalArgumentException("Unknown id: " + oid);
            }
        }
        for (int i = 0; i < orderedIds.size(); i++) {
            Testimonial e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(10_000 + i);
            repo.save(e);
        }
        repo.flush();
        for (int i = 0; i < orderedIds.size(); i++) {
            Testimonial e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(i + 1);
            repo.save(e);
        }
        return getAll();
    }

    private void normalizeOrder() {
        List<Testimonial> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            Testimonial e = all.get(i);
            int expected = i + 1;
            if (e.getDisplayOrder() == null || e.getDisplayOrder() != expected) {
                e.setDisplayOrder(expected);
                repo.save(e);
            }
        }
    }

    private void validate(Testimonial e, boolean creating) {
        if (e.getName() == null || e.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (e.getMessage() == null || e.getMessage().isBlank()) {
            throw new IllegalArgumentException("message is required");
        }
        if (e.getRating() != null && (e.getRating() < 1 || e.getRating() > 5)) {
            throw new IllegalArgumentException("rating must be between 1 and 5");
        }
    }
}